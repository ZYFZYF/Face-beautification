package com.example.face_beautification;

import android.graphics.Bitmap;

public abstract class Translater {
    abstract Bitmap render(PictureManager pictureManager, Bitmap bitmap, int level);
}
