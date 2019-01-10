package com.yakin.socialsdk.ding;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.android.dingtalk.share.ddsharemodule.message.DDMediaMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDWebpageMessage;
import com.android.dingtalk.share.ddsharemodule.message.SendMessageToDD;
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

public class DingShareProxy {

    public static void shareComplete(BaseResp resp) {
        SocialSDK.log(BusEvent.ACTION_SHARE_TO_DING.toUpperCase(), "code:" + resp.mErrCode + ", msg:" + resp.mErrStr);
        if(resp.mErrCode == BaseResp.ErrCode.ERR_OK) {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_DING, SocialResult.RESULT_SUCCEED));
        } else if(resp.mErrCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_DING, SocialResult.RESULT_CANCEL));
        } else {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_DING, new Exception("[" + resp.mErrCode + "]" + resp.mErrStr)));
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
        DDWebpageMessage webPageObject = new DDWebpageMessage();
        webPageObject.mUrl = scene.getLink();

        DDMediaMessage webMessage = new DDMediaMessage();
        webMessage.mMediaObject = webPageObject;
        webMessage.mTitle = title;
        webMessage.mContent = scene.getDesc();
        webMessage.setThumbImage(thumb);
        SendMessageToDD.Req req = new SendMessageToDD.Req();
        req.mMediaMessage = webMessage;
        req.mTransaction = String.valueOf(System.currentTimeMillis());
        IDDShareApi ddApi = DingAPI.createDDShareApi(context, appId);
        if(ddApi.isDDSupportAPI()) {
            ddApi.sendReq(req);
        } else {
            BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_DING, SocialResult.RESULT_NO_INSTALLED));
        }
        ((Activity) context).finish();
        thumb.recycle();
    }
}
