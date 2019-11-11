package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class WhiteningTranslater extends Translater {

    public WhiteningTranslater() {
        super("Whitening", "美白", false, 0);
    }

    @Override
    void render(PictureManager pictureManager, Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        for (int row = 0; row < mat.rows(); row++) {
            for (int col = 0; col < mat.cols(); col++) {
                double[] rgb = mat.get(row, col);
                rgb[0] += level;
                rgb[1] += level;
                rgb[2] += level;
                mat.put(row, col, rgb);
            }
        }
        Utils.matToBitmap(mat, bitmap);
    }

}
