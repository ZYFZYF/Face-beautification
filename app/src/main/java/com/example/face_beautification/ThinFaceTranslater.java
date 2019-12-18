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
            List<Point> rightFaceContour = pictureManager.faceLandmark.getFaceContourRight();
            Point center = pictureManager.faceLandmark.getNoseCenter();
            int targetIndex = 30;
            int translateLevel = 180 + level / 2;
            Bitmap ret = Common.localTranslate(bitmap, (int) leftFaceContour.get(targetIndex).x, (int) leftFaceContour.get(targetIndex).y, (int) center.x, (int) center.y, translateLevel);
            ret = Common.localTranslate(ret, (int) rightFaceContour.get(targetIndex).x, (int) rightFaceContour.get(targetIndex).y, (int) center.x, (int) center.y, translateLevel);
            return ret;
        } else {
            return bitmap;
        }
    }
}
