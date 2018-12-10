package com.yakin.socialsdk.weibo;

import android.content.Context;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

public class WBSDK {

    static void ensureSDKInitialized(Context context, String appKey, String url, String scope) {
        WbSdk.install(context, new AuthInfo(context, appKey, url, scope));
    }
}
