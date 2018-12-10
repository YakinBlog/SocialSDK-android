package com.yakin.socialsdk.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yakin.socialsdk.SocialConfig;
import com.yakin.socialsdk.SocialSDK;
import com.yakin.socialsdk.model.SocialScene;

public class MainActivity extends AppCompatActivity {

    private SocialScene mSocialScene = new SocialScene()
            .setTitle("Title")
            .setDesc("Desc")
            .setContent("Content")
            .setThumb("Thumb")
            .setLink("http://yakinblog.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocialSDK.sConfig = new SocialConfig()
                .setPrintLog(true)
                .setWechatAppId("wx88888888")
                .setWeiboAppKey("2099293047")
                .setQQAppId("1106709509");
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
        }
    }
}
