package com.example.face_beautification;

import android.graphics.Bitmap;

import org.opencv.core.Point;

import java.util.HashMap;

public class Common {
    final static String[] EFFECT_SET = {"Whitening", "Smoother", "Lipstick", "EnlargeEye", "ThinFace"};
    final static HashMap<String, String> EFFECT_SHOW_NAME = new HashMap<String, String>() {{
        put("Whitening", "美白");
        put("Smoother", "磨皮");
        put("Lipstick", "口红");
        put("EnlargeEye", "大眼");
        put("ThinFace", "瘦脸");
    }};

    static int checkBorderX(Bitmap bitmap, int x) {
        if (x < 0) {
            return 0;
        }
        if (x >= bitmap.getWidth()) {
            return bitmap.getWidth() - 1;
        }
        return x;
    }

    static int checkBorderY(Bitmap bitmap, int y) {
        if (y < 0) {
            return 0;
        }
        if (y >= bitmap.getHeight()) {
            return bitmap.getHeight() - 1;
        }
        return y;
    }

    //局部缩放变换
    //根据原论文，level应当是[-1,1]的数字，负数的时候是缩小，正数是放大
    static public Bitmap localScale(Bitmap bitmap, int center_x, int center_y, int scale_width, int scale_height, float level) {
        int left = checkBorderX(bitmap, center_x - scale_width);
        int right = checkBorderX(bitmap, center_x + scale_width);
        int upper = checkBorderY(bitmap, center_y - scale_height);
        int lower = checkBorderY(bitmap, center_y + scale_height);
        //要把两个轴进行一定的缩放然后变成圆
        int scale = Math.max(scale_width, scale_height);
        double width_zoom_rate = scale / scale_width;
        double height_zoom_rate = scale / scale_height;
        Bitmap ret = bitmap.copy(bitmap.getConfig(), true);
        for (int i = left; i <= right; i++) {
            for (int j = upper; j <= lower; j++) {
                double r = Math.sqrt((i - center_x) * (i - center_x) * width_zoom_rate * width_zoom_rate + (j - center_y) * (j - center_y) * height_zoom_rate * height_zoom_rate);
                if (r < scale) {
                    double cos = (i - center_x) * width_zoom_rate / r;
                    double sin = (j - center_y) * height_zoom_rate / r;
                    double new_r = (1 - (r / scale - 1) * (r / scale - 1) * level) * r;
                    int new_x = checkBorderX(bitmap, (int) (new_r * cos / width_zoom_rate + center_x));
                    int new_y = checkBorderY(bitmap, (int) (new_r * sin / height_zoom_rate + center_y));
                    ret.setPixel(i, j, bitmap.getPixel(new_x, new_y));
                }
            }
        }
        return ret;
    }

    //局部扭曲变换
    static public Bitmap localTranslate(Bitmap bitmap, int c_x, int c_y, int m_x, int m_y, int r_max) {
        //这里为了简化，认为r_max就是c到m的距离
        //int r_max = (int) Math.sqrt((m_x - c_x) * (m_x - c_x) + (m_y - c_y) * (m_y - c_y));
        //进行一定程度的缩放
        //r_max = r_max * 7 / 10;
        int left = checkBorderX(bitmap, c_x - r_max);
        int right = checkBorderX(bitmap, c_x + r_max);
        int upper = checkBorderY(bitmap, c_y - r_max);
        int lower = checkBorderY(bitmap, c_y + r_max);
        Bitmap ret = bitmap.copy(bitmap.getConfig(), true);
        for (int i = left; i <= right; i++) {
            for (int j = upper; j <= lower; j++) {
                double r = Math.sqrt((i - c_x) * (i - c_x) + (j - c_y) * (j - c_y));
                if (r < r_max) {
                    double rate = Math.pow((r_max * r_max - r * r) / (r_max * r_max - r * r + (m_x - c_x) * (m_x - c_x) + (m_y - c_y) * (m_y - c_y)), 2);
                    int new_x = checkBorderX(bitmap, (int) (i - rate * (m_x - c_x)));
                    int new_y = checkBorderY(bitmap, (int) (j - rate * (m_y - c_y)));
                    ret.setPixel(i, j, bitmap.getPixel(new_x, new_y));
                }
            }
        }
        return ret;
    }
}
