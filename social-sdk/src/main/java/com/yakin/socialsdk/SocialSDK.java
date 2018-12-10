package com.yakin.socialsdk;

import android.content.Context;
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
            log("shareToWechat", "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WECHAT, scene);
    }

    public static void shareToWechatTimeline(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.wechatAppId)) {
            log("shareToWechatTimeline", "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WECHAT_TIMELINE, scene);
    }

    public static void shareToWechatFavorite(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.wechatAppId)) {
            log("shareToWechatFavorite", "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WECHAT_FAVORITE, scene);
    }

    public static void shareToWeibo(Context context, SocialScene scene) {
        if(scene == null || sConfig == null ||
                TextUtils.isEmpty(sConfig.weiboAppKey) || TextUtils.isEmpty(sConfig.weiboRedirectrUrl)) {
            log("shareToWeibo", "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_WEIBO, scene);
    }

    public static void shareToQQ(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.qqAppId)) {
            log("shareToQQ", "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_QQ, scene);
    }

    public static void shareToQZone(Context context, SocialScene scene) {
        if(scene == null || sConfig == null || TextUtils.isEmpty(sConfig.qqAppId)) {
            log("shareToQZone", "scene:" + scene + "\nconfig:" + sConfig);
            return;
        }
        SocialProxyHandler.start(context, BusEvent.ACTION_SHARE_TO_QZONE, scene);
    }
}
