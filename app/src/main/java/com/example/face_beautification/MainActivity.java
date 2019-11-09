package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    final static String[] EFFECT_SET = {"Whitening"};

    static {
        System.loadLibrary("opencv_java");
    }

    HashMap<String, Integer> effectLevel;
    PictureManager pictureManager;
    private ImageView imageView;
    private String nowEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        imageView.setImageBitmap(bitmap);

        pictureManager = new PictureManager(bitmap);

        Button button = findViewById(R.id.button);

        nowEffect = "Whitening";
        effectLevel = new HashMap<>();
        //初始化各个美颜效果对应的level
        for (String effect : EFFECT_SET) {
            effectLevel.put(effect, 0);
        }
        //设置美颜程度拉动条
        SeekBar seekBar = findViewById(R.id.levelSelector);
        seekBar.setMax(100);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //如果是因为切换效果造成的progress修改则不进行图片的重新生成
                if (fromUser) {
                    pictureManager.changeLevel(nowEffect, progress);
                    generatePicture();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void generatePicture() {
        imageView.setImageBitmap(pictureManager.generateTargetBitmap());
    }
}
