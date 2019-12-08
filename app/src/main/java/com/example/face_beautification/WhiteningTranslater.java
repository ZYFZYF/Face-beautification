package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class WhiteningTranslater extends Translater {

    public WhiteningTranslater() {
        super("Whitening", "美白", false, 6);
    }

    @Override
    void render(PictureManager pictureManager, Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        for (int row = 0; row < mat.rows(); row++) {
            for (int col = 0; col < mat.cols(); col++) {
                double[] rgb = mat.get(row, col);
                double preBrightness =rgb[0]*0.299+rgb[1]*0.587+rgb[2]*0.114;
                double brightnessIncreRate = (Math.log(preBrightness*(level-1)+1)/Math.log(level))/preBrightness;
                rgb[0]=rgb[0]*brightnessIncreRate;
                rgb[1]=rgb[1]*brightnessIncreRate;
                rgb[2]=rgb[2]*brightnessIncreRate;
                mat.put(row, col, rgb);
            }
        }
        Utils.matToBitmap(mat, bitmap);
    }

}
