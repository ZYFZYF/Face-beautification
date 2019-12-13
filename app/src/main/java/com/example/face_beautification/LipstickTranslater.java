package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import org.opencv.core.Point;

import java.util.List;

public class LipstickTranslater extends Translater {

    public LipstickTranslater() {
        super("Lipstick", "口红", false, 0);
    }

    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        if (level > 0) {
            Path path = new Path();
            List<Point> upperLip = pictureManager.faceLandmark.getUpperLip();
            path.moveTo((float) upperLip.get(0).x, (float) upperLip.get(0).y);
            for (int i = 0; i < upperLip.size(); i++) {
                path.lineTo((float) upperLip.get((i + 1) % (upperLip.size())).x, (float) upperLip.get((i + 1) % (upperLip.size())).y);
            }
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(128, level * 255 / 100, 0, 0);
            paint.setAlpha(50);
            canvas.drawPath(path, paint);
            return bitmap;
        } else {
            return bitmap;
        }
    }
}
