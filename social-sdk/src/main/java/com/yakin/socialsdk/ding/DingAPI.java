package com.yakin.socialsdk.ding;

import android.content.Context;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;

public class DingAPI {

    static String sAppId;

    static IDDShareApi createDDShareApi(Context context, String appId) {
        sAppId = appId;
        return DDShareApiFactory.createDDShareApi(context, appId, false);
    }
}
