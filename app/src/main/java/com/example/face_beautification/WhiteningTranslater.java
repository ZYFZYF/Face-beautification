package com.example.face_beautification;

import android.graphics.Bitmap;

public class WhiteningTranslater extends Translater {
    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        return null;
    }

    @Override
    String getEffectName() {
        return "Whitening";
    }
}
