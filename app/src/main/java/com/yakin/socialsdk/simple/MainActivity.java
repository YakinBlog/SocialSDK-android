package com.yakin.socialsdk.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yakin.socialsdk.SocialConfig;
import com.yakin.socialsdk.SocialSDK;
import com.yakin.socialsdk.bus.BusEvent;
import com.yakin.socialsdk.bus.BusProvider;
import com.yakin.socialsdk.bus.IBusListener;
import com.yakin.socialsdk.model.SocialResult;
import com.yakin.socialsdk.model.SocialScene;

public class MainActivity extends AppCompatActivity implements IBusListener {

    private SocialScene mSocialScene = new SocialScene()
            .setTitle("SocialSDK")
            .setDesc("这是一个集分享与登录于一体的社交类SDK，提供了一键式社交的完整的解决方案")
            .setContent("快来和我一起分享")
            .setThumb("http://yakinblog.com/assets/img/avatar-001.jpg")
//            .setThumb("http://img.netbian.com/file/2019/0103/957a6d9181291fb01d699e4c093e338c.jpg")
            .setLink("http://yakinblog.com/");

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocialSDK.sConfig = new SocialConfig()
                .setPrintLog(true)
                .setWechatAppId("wx38bf6c7a276197a0")
                .setWeiboAppKey("2099293047")
                .setQQAppId("1106709509")
                .setAlipayAppId("2018121162507346")
                .setDingAppId("dingoa9ygtvrq4mphilr0r");

        mResultView = (TextView) findViewById(R.id.result);

        BusProvider.getInstance().registerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregisterListener(this);
    }

    public void clickShare(View view) {
        if(view.getId() == R.id.shareToWechat) {
            SocialSDK.shareToWechat(this, mSocialScene);
        } else if(view.getId() == R.id.shareToWechatTimeline) {
            SocialSDK.shareToWechatTimeline(this, mSocialScene);
        } else if(view.getId() == R.id.shareToWechatFavorite) {
            SocialSDK.shareToWechatFavorite(this, mSocialScene);
        } else if(view.getId() == R.id.shareToWeibo) {
            SocialSDK.shareToWeibo(this, mSocialScene);
        } else if(view.getId() == R.id.shareToQQ) {
            SocialSDK.shareToQQ(this, mSocialScene);
        } else if(view.getId() == R.id.shareToQZone) {
            SocialSDK.shareToQZone(this, mSocialScene);
        } else if(view.getId() == R.id.shareToQPublish) {
            SocialSDK.shareToQPublish(this, mSocialScene);
        } else if(view.getId() == R.id.shareToAlipay) {
            SocialSDK.shareToAlipay(this, mSocialScene);
        } else if(view.getId() == R.id.shareToDing) {
            SocialSDK.shareToDing(this, mSocialScene);
        } else if(view.getId() == R.id.shareToSystem) {
            SocialSDK.shareToSystem(this, mSocialScene);
        }
    }

    @Override
    public void onBusEvent(BusEvent event) {
        if(BusEvent.ACTION_SHARE_TO_WECHAT.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("微信", event.getResult()));
            } else {
                mResultView.setText("分享到微信好友失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_WECHAT_TIMELINE.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("微信朋友圈", event.getResult()));
            } else {
                mResultView.setText("分享到微信朋友圈失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_WECHAT_FAVORITE.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("微信收藏", event.getResult()));
            } else {
                mResultView.setText("分享到微信收藏失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_QQ.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("QQ好友", event.getResult()));
            } else {
                mResultView.setText("分享到QQ好友失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_QZONE.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("QQ空间", event.getResult()));
            } else {
                mResultView.setText("分享到QQ空间失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_QPUBLISH.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("QQ说说", event.getResult()));
            } else {
                mResultView.setText("分享到QQ说说失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_WEIBO.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("微博", event.getResult()));
            } else {
                mResultView.setText("分享到微博失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_DING.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("钉钉", event.getResult()));
            } else {
                mResultView.setText("分享到钉钉失败:" + event.getThrowable().getLocalizedMessage());
            }
        } else if(BusEvent.ACTION_SHARE_TO_ALIPAY.equals(event.getAction())) {
            if(event.getResult() != null) {
                mResultView.setText(getResultStr("支付宝", event.getResult()));
            } else {
                mResultView.setText("分享到支付宝失败:" + event.getThrowable().getLocalizedMessage());
            }
        }
    }

    private String getResultStr(String appName, SocialResult result) {
       if(result == SocialResult.RESULT_SUCCEED) {
           return "分享到" + appName + "成功";
       } else if(result == SocialResult.RESULT_CANCEL) {
           return "分享到" + appName + "取消";
       } else if(result == SocialResult.RESULT_NO_INSTALLED) {
           return appName + "未安装";
       }
        return "分享到" + appName + "失败";
    }
}
