package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.core.Point;

import java.util.List;

public class ThinFaceTranslater extends Translater {
    ThinFaceTranslater() {
        super("ThinFace", "瘦脸", true, 0);
    }

    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        if (level > 0) {
            List<Point> leftFaceContour = pictureManager.faceLandmark.getFaceContourLeft();
            int targetIndex = 20;
            Bitmap ret = Common.localTranslate(bitmap, (int) leftFaceContour.get(targetIndex).x, (int) leftFaceContour.get(targetIndex).y, (int) leftFaceContour.get(targetIndex).x + level * 2, (int) leftFaceContour.get(targetIndex).y);
            targetIndex = 40;
            ret = Common.localTranslate(ret, (int) leftFaceContour.get(targetIndex).x, (int) leftFaceContour.get(targetIndex).y, (int) leftFaceContour.get(targetIndex).x + level * 2, (int) leftFaceContour.get(targetIndex).y);
            return ret;
        } else {
            return bitmap;
        }
    }
}
