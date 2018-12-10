package com.yakin.socialsdk;

public class SocialConfig {

    boolean printLog;
    String wechatAppId;
    String weiboAppKey;
    String weiboRedirectrUrl = "https://api.weibo.com/oauth2/default.html";
    String weiboScope = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog,invitation_write";
    String qqAppId;

    public SocialConfig setPrintLog(boolean printLog) {
        this.printLog = printLog;
        return this;
    }

    public SocialConfig setWechatAppId(String appId) {
        this.wechatAppId = appId;
        return this;
    }

    public SocialConfig setWeiboAppKey(String appKey) {
        this.weiboAppKey = appKey;
        return this;
    }

    public SocialConfig setWeiboRedirectrUrl(String url) {
        this.weiboRedirectrUrl = url;
        return this;
    }

    public SocialConfig setWeiboScope(String scope) {
        this.weiboScope = scope;
        return this;
    }

    public SocialConfig setQQAppId(String appId) {
        this.qqAppId = appId;
        return this;
    }

    @Override
    public String toString() {
        return "wxAppId=" + wechatAppId + "\nwbAppKey=" + weiboAppKey +
                "\nwbRedirectrUrl=" + weiboRedirectrUrl + "\nweiboScope=" + weiboScope +
                "\nqqAppId=" + qqAppId;
    }
}
