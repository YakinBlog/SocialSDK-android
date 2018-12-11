package com.yakin.socialsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yakin.socialsdk.alipay.AlipayShareProxy;
import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.ding.DingShareProxy;
import com.yakin.socialsdk.model.SocialScene;
import com.yakin.socialsdk.qq.QQShareProxy;
import com.yakin.socialsdk.wechat.WXShareProxy;
import com.yakin.socialsdk.weibo.WBShareProxy;

public class SocialProxyHandler extends Activity {

    private final static String KEY_ACTION = "social_action";
    private final static String KEY_SCENE = "social_scene";

    private String mSocialAction;
    private SocialScene mSocialScene;
    private SocialConfig mSocialConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mSocialConfig = SocialSDK.sConfig;
        mSocialAction = intent.getStringExtra(KEY_ACTION);
        mSocialScene = intent.getParcelableExtra(KEY_SCENE);

        if(BusEvent.ACTION_SHARE_TO_WECHAT.equals(mSocialAction)  ||
                BusEvent.ACTION_SHARE_TO_WECHAT_TIMELINE.equals(mSocialAction) ||
                BusEvent.ACTION_SHARE_TO_WECHAT_FAVORITE.equals(mSocialAction)) {
            WXShareProxy.share(this, mSocialConfig.wechatAppId, mSocialAction, mSocialScene);
        } else if(BusEvent.ACTION_SHARE_TO_WEIBO.equals(mSocialAction)) {
            WBShareProxy.share(this, mSocialConfig.weiboAppKey, mSocialConfig.weiboRedirectrUrl,
                    mSocialConfig.weiboScope, mSocialScene);
        } else if(BusEvent.ACTION_SHARE_TO_QQ.equals(mSocialAction) ||
                BusEvent.ACTION_SHARE_TO_QZONE.equals(mSocialAction) ||
                BusEvent.ACTION_SHARE_TO_QPUBLISH.equals(mSocialAction)) {
            QQShareProxy.share(this, mSocialConfig.qqAppId, mSocialAction, mSocialScene);
        } else if(BusEvent.ACTION_SHARE_TO_ALIPAY.equals(mSocialAction)) {
            AlipayShareProxy.share(this, mSocialConfig.alipayAppId, mSocialScene);
        } else if(BusEvent.ACTION_SHARE_TO_DING.equals(mSocialAction)) {
            DingShareProxy.share(this, mSocialConfig.dingAppId, mSocialScene);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialSDK.log("onActivityResult", "data:" + data);
        if(BusEvent.ACTION_SHARE_TO_WEIBO.equals(mSocialAction)) {
            WBShareProxy.doResultIntent(data);
        } else if(BusEvent.ACTION_SHARE_TO_QQ.equals(mSocialAction) ||
                BusEvent.ACTION_SHARE_TO_QZONE.equals(mSocialAction) ||
                BusEvent.ACTION_SHARE_TO_QPUBLISH.equals(mSocialAction)) {
            QQShareProxy.onActivityResultData(mSocialAction, requestCode, resultCode, data);
        }
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    static void start(Context context, String action, SocialScene scene) {
        context.startActivity(
                new Intent(context, SocialProxyHandler.class)
                .putExtra(KEY_ACTION, action)
                .putExtra(KEY_SCENE, scene));
    }
}
