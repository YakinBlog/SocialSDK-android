package com.yakin.socialsdk.alipay;

import android.content.Context;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.IAPApi;

public class AlipayAPI {

    static String sAppId;

    static IAPApi createZFBApi(Context context, String appId) {
        sAppId = appId;
        return APAPIFactory.createZFBApi(context, appId, false);
    }
}
