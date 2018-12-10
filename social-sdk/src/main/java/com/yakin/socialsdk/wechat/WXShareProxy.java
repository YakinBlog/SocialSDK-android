package com.yakin.socialsdk.wechat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.yakin.socialsdk.SocialSDK;
import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.bus.BusProvider;
import com.yakin.socialsdk.model.SocialResult;
import com.yakin.socialsdk.model.SocialScene;
import com.yakin.socialsdk.utils.AppUtil;
import com.yakin.socialsdk.utils.IoUtil;
import com.yakin.socialsdk.utils.ThreadMgr;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class WXShareProxy {

    private static IWXCallback sCallback;

    public static void shareComplete(SendMessageToWX.Resp resp) {
        if(sCallback != null) {
            sCallback.onResult(resp.errCode, resp.errStr);
        }
    }

    public static void share(final Context context, final String appId, final String action, final SocialScene scene) {
        ThreadMgr.postTask(ThreadMgr.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                Bitmap thumb;
                try {
                    URL url = URI.create(scene.getThumb()).toURL();
                    InputStream is = (InputStream) url.getContent();
                    thumb = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    SocialSDK.log(action.toUpperCase(), e.getLocalizedMessage());
                    thumb = AppUtil.getAppIcon(context);
                }
                final byte[] thumbBytes = IoUtil.bitmapToBytes(thumb);
                ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        share(context, appId, action, scene, thumbBytes);
                    }
                });
            }
        });
    }

    private static void share(Context context, String appId, final String action, SocialScene scene, byte[] thumb) {
        sCallback = new IWXCallback() {
            @Override
            public void onResult(int code, String error) {
                SocialSDK.log(action.toUpperCase(), "code:" + code + ", error:" + error);
                if(code == BaseResp.ErrCode.ERR_OK) {
                    BusProvider.getInstance().notify(new BusEvent(action, SocialResult.RESULT_SUCCEED));
                } else if(code == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    BusProvider.getInstance().notify(new BusEvent(action, SocialResult.RESULT_CANCEL));
                } else {
                    BusProvider.getInstance().notify(new BusEvent(action, new Exception("[" + code + "]" + error)));
                }
            }
        };
        String title = scene.getTitle();
        if(TextUtils.isEmpty(title)) {
            title = AppUtil.getAppName(context);
        }

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = scene.getLink();
        WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
        mediaMessage.title = title;
        mediaMessage.description = scene.getDesc();
        mediaMessage.thumbData = thumb;
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = mediaMessage;
        IWXAPI wxApi = WXApi.createWXAPIInstance(context, appId);
        int supportVer = wxApi.getWXAppSupportAPI();
        SocialSDK.log(action.toUpperCase(), "supportVer:" + supportVer);
        if(BusEvent.ACTION_SHARE_TO_WECHAT_TIMELINE.equals(action)) {
            if (supportVer >= Build.TIMELINE_SUPPORTED_SDK_INT) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            }
        } else if(BusEvent.ACTION_SHARE_TO_WECHAT_FAVORITE.equals(action)) {
            if (supportVer >= Build.FAVORITE_SUPPPORTED_SDK_INT) {
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            }
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }

        wxApi.sendReq(req);
        ((Activity) context).finish();
    }
}
