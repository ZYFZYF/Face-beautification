package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


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


        nowEffect = "Whitening";
        effectLevel = new HashMap<>();
        //初始化各个美颜效果对应的level
        for (String effect : Common.EFFECT_SET) {
            effectLevel.put(effect, 0);
        }
        //设置美颜程度拉动条
        final SeekBar seekBar = findViewById(R.id.levelSelector);
        seekBar.setMax(100);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //如果是因为切换效果造成的progress修改则不进行图片的重新生成
                effectLevel.put(nowEffect, progress);
                if (fromUser) {
                    pictureManager.changeLevel(nowEffect, progress);
                    generatePicture();
                }
                System.out.println("This is " + nowEffect + " and his level is " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //添加下方的选择按钮
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        for (final String effect : Common.EFFECT_SET) {
            Button button = new Button(this);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowEffect = effect;
                    seekBar.setProgress(effectLevel.get(nowEffect));
                }
            });
            button.setText(effect);
            linearLayout.addView(button);
        }
    }

    private void generatePicture() {
        imageView.setImageBitmap(pictureManager.generateTargetBitmap());
    }
}
