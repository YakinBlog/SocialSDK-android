package com.yakin.socialsdk.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yakin.socialsdk.SocialSDK;
import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.bus.BusProvider;
import com.yakin.socialsdk.model.SocialResult;
import com.yakin.socialsdk.model.SocialScene;
import com.yakin.socialsdk.utils.AppUtil;
import com.yakin.socialsdk.utils.FileUtil;
import com.yakin.socialsdk.utils.ThreadMgr;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class QQShareProxy {

    private static class UiListener implements IUiListener {
        public String action;

        @Override
        public void onComplete(Object o) {
            SocialSDK.log(action.toUpperCase(), "QQShareSuccess");
            BusProvider.getInstance().notify(new BusEvent(action, SocialResult.RESULT_SUCCEED));
        }

        @Override
        public void onError(UiError uiError) {
            SocialSDK.log(action.toUpperCase(), "QQShareFile:[" + uiError.errorCode + "]" + uiError.errorMessage);
            BusProvider.getInstance().notify(new BusEvent(action, new Exception("[" + uiError.errorCode + "]" + uiError.errorMessage)));
        }

        @Override
        public void onCancel() {
            SocialSDK.log(action.toUpperCase(), "QQShareCancel");
            BusProvider.getInstance().notify(new BusEvent(action, SocialResult.RESULT_CANCEL));
        }
    }
    private static String sThumbPath;
    private static UiListener sListener = new UiListener();

    public static void onActivityResultData(String action, int reqCode, int resCode, Intent data) {
        sListener.action = action;
        TencentAPI.onActivityResultData(reqCode, resCode, data, sListener);
        FileUtil.delete(sThumbPath);
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
                sThumbPath = context.getExternalCacheDir().getAbsolutePath() + "/share_thumb.png";
                SocialSDK.log(action.toUpperCase(), "file:" + sThumbPath);
                if(FileUtil.saveBitmap(sThumbPath, thumb)) {
                    ThreadMgr.postTask(ThreadMgr.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            share(context, appId, action, scene, sThumbPath);
                        }
                    });
                }
            }
        });
    }

    private static void share(Context context, String appId, final String action, SocialScene scene, String thumb) {
        String title = scene.getTitle();
        if(TextUtils.isEmpty(title)) {
            title = AppUtil.getAppName(context);
        }
        Tencent tencentApi = TencentAPI.createTencentInstance(context, appId);
        final Bundle bundle = new Bundle();
        sListener.action = action;
        if(BusEvent.ACTION_SHARE_TO_QQ.equals(action)) {
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, scene.getDesc());
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, scene.getLink());
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumb);
            tencentApi.shareToQQ((Activity) context, bundle, sListener);
        } else if(BusEvent.ACTION_SHARE_TO_QZONE.equals(action)) {
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, scene.getDesc());
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, scene.getLink());
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(thumb);
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
            tencentApi.shareToQzone((Activity) context, bundle, sListener);
        } else if(BusEvent.ACTION_SHARE_TO_QPUBLISH.equals(action)) {
            bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
            bundle.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, "#" + title + "#" + scene.getDesc() + scene.getLink());
            tencentApi.publishToQzone((Activity) context, bundle, sListener);
        }
    }
}
