package com.yakin.socialsdk.ding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;

public class DingCallbackActivity extends Activity implements IDDAPIEventHandler {

    private IDDShareApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = DingAPI.createDDShareApi(this, DingAPI.sAppId);
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
        DingShareProxy.shareComplete(resp);
        finish();
    }
}
