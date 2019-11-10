package com.example.face_beautification;

import android.graphics.Bitmap;

public class SmootherTranslater extends Translater {
    public SmootherTranslater() {
        super("Smoother", "磨皮", false, 0);
    }

    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        return null;
    }
}
