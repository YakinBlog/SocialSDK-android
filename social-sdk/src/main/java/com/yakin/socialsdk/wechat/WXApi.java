package com.yakin.socialsdk.wechat;

import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXApi {

    private static IWXAPI sApi;

    static IWXAPI createWXAPIInstance(Context context, String appId) {
        sApi = WXAPIFactory.createWXAPI(context, appId, true);
        sApi.registerApp(appId);
        return sApi;
    }

    static void registerHandler(Intent intent, IWXAPIEventHandler eventHandler) {
        if(sApi != null) {
            sApi.handleIntent(intent, eventHandler);
        }
    }
}
