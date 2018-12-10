package com.yakin.socialsdk.model;

public enum SocialResult {

    RESULT_SUCCEED(0),
    RESULT_CANCEL(1);

    private int result;

    SocialResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
