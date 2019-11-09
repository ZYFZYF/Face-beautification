package com.example.face_beautification;

import android.graphics.Bitmap;

public abstract class Translater {
    protected int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    abstract Bitmap render(PictureManager pictureManager, Bitmap bitmap);

    abstract String getEffectName();
}
