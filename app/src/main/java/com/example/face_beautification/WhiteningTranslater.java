package com.example.face_beautification;

import android.graphics.Bitmap;

public class WhiteningTranslater extends Translater {

    public WhiteningTranslater() {
        super("Whitening", "美白", false, 0);
    }

    //注意不要修改传进来的值
    @Override
    Bitmap render(PictureManager pictureManager, Bitmap bitmap) {
        return bitmap;
    }

}
