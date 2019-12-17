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
        super("Whitening", "美白", true, 0);
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
            //Imgproc.accumulate(test, mat);

            double[] pixel=new double[3];
            for (int i = 0, rlen = mat.rows(); i < rlen; i++) {
                for (int j = 0, clen = mat.cols(); j < clen; j++) {
                        pixel = mat.get(i, j).clone();
                        double brightness=0.299*pixel[0]+0.587*pixel[1]+0.114*pixel[2];
                        double brightnessIncre=(Math.log(brightness*(0.2*level-1)+1)/Math.log(0.2*level))-brightness;//level限制在0-20
                        pixel[0] = pixel[0]+brightnessIncre/(0.299+0.587+0.114);//R
                        pixel[1] = pixel[1]+brightnessIncre/(0.299+0.587+0.114);//G
                        pixel[2] = pixel[2]+brightnessIncre/(0.299+0.587+0.114);//B
                        mat.put(i, j, pixel);

                }
            }

            mat.convertTo(mat, CvType.CV_8UC3, 255);

        }
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

}
