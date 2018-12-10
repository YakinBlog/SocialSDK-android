package com.yakin.socialsdk.utils;

import android.graphics.Bitmap;

import com.yakin.socialsdk.SocialSDK;

import java.io.ByteArrayOutputStream;
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

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            data = baos.toByteArray();
        } catch (Exception e) {
            SocialSDK.log("bitmapToBytes", e.getLocalizedMessage());
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) { }
        }
        return data;
    }
}
