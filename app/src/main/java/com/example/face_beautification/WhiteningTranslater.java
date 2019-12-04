package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

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

        Utils.matToBitmap(mat, bitmap);
    }

}
