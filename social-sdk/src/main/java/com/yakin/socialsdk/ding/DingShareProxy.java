package com.yakin.socialsdk.ding;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.android.dingtalk.share.ddsharemodule.message.DDMediaMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDWebpageMessage;
import com.android.dingtalk.share.ddsharemodule.message.SendMessageToDD;
import com.yakin.socialsdk.SocialSDK;
import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.model.SocialScene;
import com.yakin.socialsdk.utils.AppUtil;
import com.yakin.socialsdk.utils.ThreadMgr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class DingShareProxy {

    public static void shareComplete(BaseResp resp) {
        SocialSDK.log(BusEvent.ACTION_SHARE_TO_DING.toUpperCase(), "code:" + resp.mErrCode + ", msg:" + resp.mErrStr);
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
                final Bitmap thumbBitmap = getCompressBitmap(thumb);
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
        thumb.recycle();
        SendMessageToDD.Req req = new SendMessageToDD.Req();
        req.mMediaMessage = webMessage;
        req.mTransaction = String.valueOf(System.currentTimeMillis());
        DingAPI.createDDShareApi(context, appId).sendReq(req);
        ((Activity) context).finish();
    }

    private static Bitmap getCompressBitmap(Bitmap thumb) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        ByteArrayInputStream bitmapStream = new ByteArrayInputStream(stream.toByteArray());
        return BitmapFactory.decodeStream(bitmapStream, null, opts);
    }
}
