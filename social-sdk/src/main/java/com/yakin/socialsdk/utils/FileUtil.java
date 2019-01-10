package com.yakin.socialsdk.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yakin.socialsdk.SocialSDK;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static boolean saveBitmap(String filePath, Bitmap bmp) {
        FileOutputStream fileout = null;
        try {
            if(bmp != null && !bmp.isRecycled()) {
                fileout = new FileOutputStream(filePath);
                BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileout);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bufferOutStream);
                bufferOutStream.flush();
                return true;
            }
        } catch (IOException e) {
            SocialSDK.log("saveBitmap", e.getLocalizedMessage());
        } finally {
            IoUtil.safeClose(fileout);
        }
        return false;
    }

    public static void delete(String filePath) {
        if(!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
