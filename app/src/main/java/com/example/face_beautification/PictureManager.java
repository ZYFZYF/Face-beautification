package com.example.face_beautification;

import android.graphics.Bitmap;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
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
        try {

            translaters = new ArrayList<>();
            for (String effect : Common.EFFECT_SET) {
                translaters.add((Translater) Class.forName("com.example.face_beautification." + effect + "Translater").newInstance());
                System.out.println(translaters.get(0).getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //如果出现异常，说明没get到正确的信息，再跑一遍就好了
                for (int i = 0; i < 3; i++) {
                    String respones = RemoteApi.getFaceLandmarks(bitmap);
                    try {
                        JSONObject jsonObject = new JSONObject(new JSONTokener(respones));
                        System.out.println(jsonObject);
                        faceLandmark = new FaceLandmark(jsonObject);
                        break;
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Bitmap generateTargetBitmap() {
        targetBitmap = originBitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (Translater translater : translaters) {
            long begin = System.currentTimeMillis();
            targetBitmap = translater.render(this, targetBitmap);
            System.out.printf("%s %d render cost %d ms\n", translater.getName(), translater.getLevel(), System.currentTimeMillis() - begin);
        }
        return targetBitmap;
    }

    public void changeLevel(String effect, int targetLevel) {
        for (Translater translater : translaters) {
            if (translater.getName().equals(effect)) {
                translater.setLevel(targetLevel);
            }
        }
    }

}
