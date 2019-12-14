package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.core.Point;

public class EnlargeEyeTranslater extends Translater {
    EnlargeEyeTranslater() {
        super("EnlargeEye", "大眼", true, 0);
    }

    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        if (level > 0) {
            Point left_eye_center = pictureManager.faceLandmark.getLeftEyePupleCenter();
            int left_eye_radius = pictureManager.faceLandmark.getLeftEyePupleRadius();
            return Common.localScale(bitmap, (int) left_eye_center.x, (int) left_eye_center.y, left_eye_radius * 3, (float) level / 50 - 1);
        } else {
            return bitmap;
        }
    }
}
