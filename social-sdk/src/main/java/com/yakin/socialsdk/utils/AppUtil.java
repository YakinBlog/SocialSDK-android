package com.yakin.socialsdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.yakin.socialsdk.SocialSDK;

public class AppUtil {

    private static String sAppName;

    public static String getAppName(Context context) {
        if(TextUtils.isEmpty(sAppName)) {
            try {
                PackageManager pManager = context.getPackageManager();
                ApplicationInfo appInfo = pManager.getApplicationInfo(context.getPackageName(), 0);
                sAppName = pManager.getApplicationLabel(appInfo).toString();
            } catch (PackageManager.NameNotFoundException e) {
                SocialSDK.log("getAppName", e.getLocalizedMessage());
            }
        }
        return sAppName;
    }

    public static Bitmap getAppIcon(Context context) {
        try {
            PackageManager pManager = context.getPackageManager();
            Drawable drawable = pManager.getApplicationIcon(context.getPackageName());
            if(drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable)drawable).getBitmap();
            }
        } catch (PackageManager.NameNotFoundException e) {
            SocialSDK.log("getAppIcon", e.getLocalizedMessage());
        }
        return null;
    }
}
