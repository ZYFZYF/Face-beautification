package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class WhiteningTranslater extends Translater {

    public WhiteningTranslater() {
        super("Whitening", "美白", false, 0);
    }

    @Override
    void render(PictureManager pictureManager, Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        //如果用到了这个效果再做
        if (level > 0) {
            Imgproc.blur(mat, mat, new Size(level, level));
        }
        //显示脸部轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        contours.add(new MatOfPoint(pictureManager.faceLandmark.getFaceContour()));
        Imgproc.drawContours(mat, contours, 0, new Scalar(255, 255, 255));
        Utils.matToBitmap(mat, bitmap);
    }

}
