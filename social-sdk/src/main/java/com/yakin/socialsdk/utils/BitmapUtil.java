package com.yakin.socialsdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static Bitmap compressBitmap(Bitmap bitmap, int maxSize) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            BitmapFactory.Options opts = new BitmapFactory.Options();
            int minSide = Math.min(bitmap.getWidth(), bitmap.getHeight());
            double sqrtSize = Math.sqrt(maxSize); // 目标是正方形
            int sampleSize = 1;
            if(minSide > sqrtSize) {
                int ratio = (int) Math.ceil(minSide / sqrtSize);
                for (int pow = 1; sampleSize < ratio; pow ++) {
                    sampleSize = (int) Math.pow(2, pow);
                }
            }
            opts.inSampleSize = sampleSize * 2; // 避免高清图
            opts.inScaled = true;
            Bitmap newBitmap = BitmapFactory.decodeStream(bais, null, opts);
            bais.close();
            baos.close();
            return newBitmap;
        } catch (Exception e) { }
        return bitmap;
    }

    public static Bitmap fillAlphaPlace(Bitmap bitmap, int color) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(color);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        return newBitmap;
    }
}
