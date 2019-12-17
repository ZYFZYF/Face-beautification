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
            Bitmap ret = Common.localScale(bitmap, (int) left_eye_center.x, (int) left_eye_center.y, left_eye_radius * 3, left_eye_radius * 3, (float) level / 200);
            Point right_eye_center = pictureManager.faceLandmark.getRightEyePupleCenter();
            int right_eye_radius = pictureManager.faceLandmark.getRightEyePupleRadius();
            return Common.localScale(ret, (int) right_eye_center.x, (int) right_eye_center.y, right_eye_radius * 3, right_eye_radius * 3, (float) level / 200);
        } else {
            return bitmap;
        }
    }
}
