package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

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
        if (level > 0) {
            int clen = bitmap.getWidth(), rlen = bitmap.getHeight();
            int pixel;
            int[] newPixels = new int[rlen * clen];

            for (int i = 0; i < rlen; i++) {
                for (int j = 0; j < clen; j++) {
                    pixel = bitmap.getPixel(j, i);
                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    double brightness = 0.299 * r + 0.587 * g + 0.114 * b;
                    double brightnessIncre = (((Math.log((brightness / 255) * 0.02 * level + 1) / Math.log(0.02 * level + 1))) - brightness / 255) * 255;
                    r = r + (int) brightnessIncre > 255 ? 255 : r + (int) brightnessIncre;//R
                    g = g + (int) brightnessIncre > 255 ? 255 : g + (int) brightnessIncre;//G
                    b = b + (int) brightnessIncre > 255 ? 255 : b + (int) brightnessIncre;//B
                    newPixels[i * clen + j] = Color.rgb(r, g, b);
                }
            }
            bitmap.setPixels(newPixels, 0, clen, 0, 0, clen, rlen);
            return bitmap;
        } else {
            return bitmap;
        }
    }
}
