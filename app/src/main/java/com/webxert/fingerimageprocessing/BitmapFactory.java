package com.webxert.fingerimageprocessing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by hp on 9/6/2019.
 */

public class BitmapFactory {
    public static Bitmap getGrayScaleBitmap(Bitmap colorBitmap) {
        Bitmap grayscaleBitmap = Bitmap.createBitmap(
                colorBitmap.getWidth(), colorBitmap.getHeight(),
                Bitmap.Config.RGB_565);

        return grayscaleBitmap;
    }

    public static Bitmap drawBitmap(Bitmap original, Bitmap toDraw) {
        Canvas c = new Canvas(toDraw);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();

        cm.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        p.setColorFilter(filter);
        c.drawBitmap(original, 0, 0, p);
        return toDraw;
    }
}
