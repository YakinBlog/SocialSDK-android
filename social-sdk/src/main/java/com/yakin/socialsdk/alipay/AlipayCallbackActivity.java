package com.yakin.socialsdk.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alipay.share.sdk.openapi.BaseReq;
import com.alipay.share.sdk.openapi.BaseResp;
import com.alipay.share.sdk.openapi.IAPAPIEventHandler;
import com.alipay.share.sdk.openapi.IAPApi;

public class AlipayCallbackActivity extends Activity implements IAPAPIEventHandler {

    private IAPApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = AlipayAPI.createZFBApi(this, AlipayAPI.sAppId);
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        AlipayShareProxy.shareComplete(resp);
        finish();
    }
}
