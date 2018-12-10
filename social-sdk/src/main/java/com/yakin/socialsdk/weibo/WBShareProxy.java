package com.yakin.socialsdk.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
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

public class WBShareProxy {

    private static WbShareHandler sHandler;

    public static void doResultIntent(Intent intent) {
        if(sHandler != null) {
            if(intent == null || intent.getExtras() == null) {
                SocialSDK.log("shareToWeibo", "WbShareError");
                BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_WEIBO, new Exception("startup weibo failed")));
                return;
            }
            sHandler.doResultIntent(intent, new WbShareCallback() {
                @Override
                public void onWbShareSuccess() {
                    SocialSDK.log("shareToWeibo", "WbShareSuccess");
                    BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_WEIBO, SocialResult.RESULT_SUCCEED));
                }

                @Override
                public void onWbShareCancel() {
                    SocialSDK.log("shareToWeibo", "WbShareCancel");
                    BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_WEIBO, SocialResult.RESULT_CANCEL));
                }

                @Override
                public void onWbShareFail() {
                    SocialSDK.log("shareToWeibo", "WbShareFail");
                    BusProvider.getInstance().notify(new BusEvent(BusEvent.ACTION_SHARE_TO_WEIBO, new Exception("unknow")));
                }
            });
        }
    }

    public static void share(final Context context, final String appKey, final String url, final String scope, final SocialScene scene) {
        ThreadMgr.postTask(ThreadMgr.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                Bitmap thumb;
                try {
                    URL url = URI.create(scene.getThumb()).toURL();
                    InputStream is = (InputStream) url.getContent();
                    thumb = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    SocialSDK.log("shareToWeibo", e.getLocalizedMessage());
                    thumb = AppUtil.getAppIcon(context);
                }
                final byte[] thumbBytes = IoUtil.bitmapToBytes(thumb);
                ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        share(context, appKey, url, scope, scene, thumbBytes);
                    }
                });
            }
        });
    }

    private static void share(Context context, String appKey, String url, String scope, SocialScene scene, byte[] thumb) {
        String title = scene.getTitle();
        if(TextUtils.isEmpty(title)) {
            title = AppUtil.getAppName(context);
        }
        WBSDK.ensureSDKInitialized(context, appKey, url, scope);
        sHandler = new WbShareHandler((Activity) context);
        sHandler.registerApp();
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = scene.getContent();
        weiboMessage.textObject = textObject;
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.title = title;
        webpageObject.description = scene.getDesc();
        webpageObject.actionUrl = scene.getLink();
        webpageObject.thumbData = thumb;
        weiboMessage.mediaObject = webpageObject;
        sHandler.shareMessage(weiboMessage, false);
    }
}
