package com.example.face_beautification;

import android.graphics.Bitmap;

public class WhiteningTranslater extends Translater {
    //注意不要修改传进来的值
    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        return bitmap;
    }

    @Override
    String getEffectName() {
        return "Whitening";
    }
}
