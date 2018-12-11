package com.yakin.socialsdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.model.SocialScene;

public class SocialSDK {

    public static SocialConfig sConfig;

    public static void log(String tag, String msg) {
        if(sConfig.printLog) {
            Log.d("SocialSDK_" + tag, msg);
        }
    }

    public static void shareToWechat(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.wechatAppId)) {
            log(BusEvent.ACTION_SHARE_TO_WECHAT.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WECHAT, scene);
    }

    public static void shareToWechatTimeline(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.wechatAppId)) {
            log(BusEvent.ACTION_SHARE_TO_WECHAT_TIMELINE.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WECHAT_TIMELINE, scene);
    }

    public static void shareToWechatFavorite(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.wechatAppId)) {
            log(BusEvent.ACTION_SHARE_TO_WECHAT_FAVORITE.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WECHAT_FAVORITE, scene);
    }

    public static void shareToWeibo(Context context, SocialScene scene) {
        if(scene == null || sConfig == null ||
                TextUtils.isEmpty(sConfig.weiboAppKey) || TextUtils.isEmpty(sConfig.weiboRedirectrUrl)) {
            log(BusEvent.ACTION_SHARE_TO_WEIBO.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WEIBO, scene);
    }

    public static void shareToQQ(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.qqAppId)) {
            log(BusEvent.ACTION_SHARE_TO_QQ.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_QQ, scene);
    }

    public static void shareToQZone(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.qqAppId)) {
            log(BusEvent.ACTION_SHARE_TO_QZONE.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_QZONE, scene);
    }

    public static void shareToAlipay(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.alipayAppId)) {
            log(BusEvent.ACTION_SHARE_TO_ALIPAY.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_ALIPAY, scene);
    }

    public static void shareToDing(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.dingAppId)) {
            log(BusEvent.ACTION_SHARE_TO_DING.toUpperCase(), "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_DING, scene);
    }

    public static void shareToSystem(Context context, SocialScene scene) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "#" + scene.getTitle() + "#" + scene.getDesc() + scene.getLink());
        context.startActivity(Intent.createChooser(shareIntent, "分享"));
    }
}
