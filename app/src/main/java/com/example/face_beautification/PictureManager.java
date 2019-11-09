package com.example.face_beautification;

import android.graphics.Bitmap;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

public class PictureManager {
    Bitmap originBitmap, targetBitmap;
    FaceLandmark faceLandmark;
    List<Translater> translaters;

    PictureManager(final Bitmap bitmap) {
        faceLandmark = null;
        originBitmap = bitmap;
        targetBitmap = null;
        //这里要按照顺序添加所有实现了的变换，因为会按照顺序执行一个个变换
        //因为关键点只在最开始获取了一次，所以最后再做会改变关键点的操作，例如瘦脸
        translaters.add(new WhiteningTranslater());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //如果出现异常，说明没get到正确的信息，再跑一遍就好了
                while (true) {
                    String respones = RemoteApi.getFaceLandmarks(bitmap);
                    try {
                        JSONObject jsonObject = new JSONObject(new JSONTokener(respones));
                        faceLandmark = new FaceLandmark(jsonObject);
                        break;
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public void generateTargetBitmap() {
        targetBitmap = originBitmap;
        for (Translater translater : translaters) {
            targetBitmap = translater.render(this, targetBitmap);
        }
    }

    public void changeLevel(String effect, int targetLevel) {
        for (Translater translater : translaters) {
            if (translater.getEffectName().equals(effect)) {
                translater.setLevel(targetLevel);
            }
        }
    }


}
