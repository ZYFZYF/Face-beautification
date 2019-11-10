package com.example.face_beautification;

import android.graphics.Bitmap;

public abstract class Translater {
    protected int level;
    boolean changeLandmark;
    String name, nameZh;

    Translater(String name, String nameZh, Boolean changeLandmark, int level) {
        this.name = name;
        this.nameZh = nameZh;
        this.changeLandmark = changeLandmark;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public boolean isChangeLandmark() {
        return changeLandmark;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    ;

    abstract Bitmap render(PictureManager pictureManager, Bitmap bitmap);
}
