package com.yakin.socialsdk.utils;

import com.yakin.socialsdk.SocialSDK;

import java.io.Closeable;

public class IoUtil {

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                SocialSDK.log("safeClose", e.getLocalizedMessage());
            }
        }
    }
}
