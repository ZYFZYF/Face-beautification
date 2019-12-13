package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class WhiteningTranslater extends Translater {

    public WhiteningTranslater() {
        super("Whitening", "美白", false, 0);
    }

    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        //去掉透明通道
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        //如果用到了这个效果再做
        if (level > 0) {
            //Imgproc.blur(mat, mat, new Size(level, level));
            //显示脸部轮廓
            List<MatOfPoint> contours = new ArrayList<>();
            contours.add(new MatOfPoint(pictureManager.faceLandmark.getFaceContour()));
            Imgproc.drawContours(mat, contours, 0, new Scalar(255, 255, 255));
            System.out.println(mat.type());
            System.out.println(CvType.typeToString(mat.type()));
            Mat test = new Mat(mat.size(), CvType.CV_32FC3, new Scalar(0.01 * level / 100, 0.01 * level, 0.01 * level));
            mat.convertTo(mat, CvType.CV_32FC3, 1.0 / 255);
            Imgproc.accumulate(test, mat);
            mat.convertTo(mat, CvType.CV_8UC3, 255);

        }
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

}
