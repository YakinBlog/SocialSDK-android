package com.yakin.socialsdk.qq;

import android.content.Context;
import android.content.Intent;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

public class TencentAPI {

    static Tencent createTencentInstance(Context context, String appId) {
        return Tencent.createInstance(appId, context);
    }

    static void onActivityResultData(int reqCode, int resCode, Intent data, IUiListener listener) {
        Tencent.onActivityResultData(reqCode, resCode, data, listener);
    }
}
