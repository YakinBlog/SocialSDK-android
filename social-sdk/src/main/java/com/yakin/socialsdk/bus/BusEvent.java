package com.yakin.socialsdk.bus;

import com.yakin.socialsdk.model.SocialResult;

public class BusEvent {

    public static final String ACTION_SHARE_TO_WECHAT = "action_share_to_wechat";
    public static final String ACTION_SHARE_TO_WECHAT_TIMELINE = "action_share_to_wechat_timeline";
    public static final String ACTION_SHARE_TO_WECHAT_FAVORITE = "action_share_to_wechat_favorite";
    public static final String ACTION_SHARE_TO_WEIBO = "action_share_to_weibo";
    public static final String ACTION_SHARE_TO_QQ = "action_share_to_qq";
    public static final String ACTION_SHARE_TO_QZONE = "action_share_to_qzone";
    public static final String ACTION_SHARE_TO_ALIPAY = "action_share_to_alipay";
    public static final String ACTION_SHARE_TO_DING = "action_share_to_ding";

    private String action;
    private SocialResult result;
    private Throwable throwable;

    public BusEvent(String action, SocialResult result) {
        this.action = action;
        this.result = result;
    }

    public BusEvent(String action, Throwable throwable) {
        this.action = action;
        this.throwable = throwable;
    }

    public String getAction() {
        return action;
    }

    public SocialResult getResult() {
        return result;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
