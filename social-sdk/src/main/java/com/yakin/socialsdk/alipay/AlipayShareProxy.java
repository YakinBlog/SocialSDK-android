package com.yakin.socialsdk.alipay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.BaseResp;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.yakin.socialsdk.SocialSDK;
import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.bus.BusProvider;
import com.yakin.socialsdk.model.SocialResult;
import com.yakin.socialsdk.model.SocialScene;
import com.yakin.socialsdk.utils.AppUtil;
import com.yakin.socialsdk.utils.BitmapUtil;
import com.yakin.socialsdk.utils.ThreadMgr;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class AlipayShareProxy {

    public static void shareComplete(BaseResp resp) {
        SocialSDK.log(BusEvent.ACTION_SHARE_TO_ALIPAY.toUpperCase(), "code:" + resp.errCode + ", msg:" + resp.errStr);
        if(resp.errCode == com.android.dingtalk.share.ddsharemodule.message.BaseResp.ErrCode.ERR_OK) {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_ALIPAY, SocialResult.RESULT_SUCCEED));
        } else if(resp.errCode == com.android.dingtalk.share.ddsharemodule.message.BaseResp.ErrCode.ERR_USER_CANCEL) {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_ALIPAY, SocialResult.RESULT_CANCEL));
        } else {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_ALIPAY, new Exception("[" + resp.errCode + "]" + resp.errStr)));
        }
    }

    public static void share(final Context context, final String appId, final SocialScene scene) {
        ThreadMgr.postTask(ThreadMgr.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                Bitmap thumb;
                try {
                    URL url = URI.create(scene.getThumb()).toURL();
                    InputStream is = (InputStream) url.getContent();
                    thumb = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    SocialSDK.log(BusEvent.ACTION_SHARE_TO_ALIPAY.toUpperCase(), e.getLocalizedMessage());
                    thumb = AppUtil.getAppIcon(context);
                }
                final Bitmap thumbBitmap = BitmapUtil.compressBitmap(thumb, 32 * 1024);
                thumb.recycle();
                ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        share(context, appId, scene, thumbBitmap);
                    }
                });
            }
        });
    }

    private static void share(Context context, String appId, SocialScene scene, Bitmap thumb) {
        String title = scene.getTitle();
        if(TextUtils.isEmpty(title)) {
            title = AppUtil.getAppName(context);
        }
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = scene.getLink();

        APMediaMessage webMessage = new APMediaMessage();
        webMessage.mediaObject = webPageObject;
        webMessage.title = title;
        webMessage.description = scene.getDesc();
        webMessage.setThumbImage(thumb);
        SendMessageToZFB.Req req = new SendMessageToZFB.Req();
        req.message = webMessage;
        req.transaction = String.valueOf(System.currentTimeMillis());
        IAPApi alipayApi = AlipayAPI.createZFBApi(context, appId);
        if(alipayApi.isZFBAppInstalled()) {
            if (alipayApi.getZFBVersionCode() < 101) { // 9.9.5版本（VersionCode为101）开始无需判断分享场景
                req.scene = SendMessageToZFB.Req.ZFBSceneTimeLine;
            }
            alipayApi.sendReq(req);
        } else {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_ALIPAY, SocialResult.RESULT_NO_INSTALLED));
        }
        ((Activity) context).finish();
        thumb.recycle();
    }
}
