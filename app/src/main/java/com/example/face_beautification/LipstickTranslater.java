package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import org.opencv.core.Point;

import java.util.List;

public class LipstickTranslater extends Translater {

    public LipstickTranslater() {
        super("Lipstick", "口红", false, 0);
    }

    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        if (level > 0) {
            Path upperPart = new Path();
            List<Point> upperLip = pictureManager.faceLandmark.getUpperLip();
            upperPart.moveTo((float) upperLip.get(0).x, (float) upperLip.get(0).y);
            for (int i = 0; i < upperLip.size(); i++) {
                upperPart.lineTo((float) upperLip.get((i + 1) % (upperLip.size())).x, (float) upperLip.get((i + 1) % (upperLip.size())).y);
            }
            Path lowerPart = new Path();
            List<Point> lowerLip = pictureManager.faceLandmark.getLowerLip();
            lowerPart.moveTo((float) lowerLip.get(0).x, (float) lowerLip.get(0).y);
            for (int i = 0; i < lowerLip.size(); i++) {
                lowerPart.lineTo((float) lowerLip.get((i + 1) % (lowerLip.size())).x, (float) lowerLip.get((i + 1) % (lowerLip.size())).y);
            }
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            //给画图加个模糊
            paint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(255, 80 + level * 140 / 100, 0, 0);
            paint.setAlpha(70);
            canvas.drawPath(upperPart, paint);
            canvas.drawPath(lowerPart, paint);
            return bitmap;
        } else {
            return bitmap;
        }
    }
}
