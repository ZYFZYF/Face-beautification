package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SmootherTranslater extends Translater {
    public SmootherTranslater() {
        super("Smoother", "磨皮", false, 0);
    }

    @Override
    void render(PictureManager pictureManager, Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        //去掉透明通道
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        //如果用到了这个效果再做
        if (level > 6) {
            int value1 = level / 2, value2 = level / 6;
            Mat temp1 = new Mat();
            Mat temp2 = new Mat();
            Mat temp3 = new Mat();
            Mat temp4 = new Mat();
            Imgproc.bilateralFilter(mat, temp1, value1, value1 * 2, value1 / 2);
            Core.addWeighted(temp1, 1, mat, -1, 128, temp2);
            Imgproc.GaussianBlur(temp2, temp3, new Size(2 * value2 - 1, 2 * value2 - 1), 0, 0);
            Core.addWeighted(mat, 1, temp3, 2, -255, temp4);
            Core.addWeighted(mat, 0.5, temp4, 0.5, 0, mat);
        }
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(mat, bitmap);
    }
}
