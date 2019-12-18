package com.example.face_beautification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {
//    static {
//        System.loadLibrary("opencv_java");
//    }

    final int TAKE_PHOTO_REQUEST = 0;
    HashMap<String, Integer> effectLevel;
    PictureManager pictureManager;
    private ImageView imageView;
    private String nowEffect;
    private SeekBar seekBar;
    private TabHost tabHost;
    private boolean needRender;
    private long prevChangedTime;
    private Timer timer;
    private TimerTask timerTask;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPopupWindow();
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_12);
        initImageView(bitmap);
        initTranslater(bitmap);
        initSeekBar();
        initTabHost();
        LinearLayout page1 = (LinearLayout) findViewById(R.id.page1);
        page1.setAlpha(0.5f);

    }

    private void initImageView(Bitmap bitmap) {
        //设置显示的默认图片，以及点击回调
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(findViewById(R.id.linearLayout), Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private void initTranslater(Bitmap bitmap) {
        //新建图片管理器
        pictureManager = new PictureManager(bitmap);


        nowEffect = "Whitening";
        effectLevel = new HashMap<>();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (needRender && System.currentTimeMillis() - prevChangedTime > 100) {
                    imageView.setImageBitmap(pictureManager.generateTargetBitmap());
                    needRender = false;
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 100);

        //初始化各个美颜效果对应的level
        for (String effect : Common.EFFECT_SET) {
            effectLevel.put(effect, 0);
        }
    }

    private void initSeekBar() {
        //设置美颜程度拉动条
        seekBar = findViewById(R.id.levelSelector);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //如果是因为切换效果造成的progress修改则不进行图片的重新生成
                System.out.printf("changed and progress is %d and fromUser is %b\n", progress, fromUser);
                effectLevel.put(nowEffect, progress);
                final int myProgress = progress;
                if (fromUser) {
                    pictureManager.changeLevel(nowEffect, myProgress);
                    needRender = true;
                    prevChangedTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(0);
    }

    private void initTabHost() {
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        for (String effect : Common.EFFECT_SET) {
            tabHost.addTab(tabHost.newTabSpec(effect).setIndicator(Common.EFFECT_SHOW_NAME.get(effect)).setContent(R.id.linearLayout));
        }
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                System.out.printf("some thing changed %s\n", tabId);
                nowEffect = tabId;
                seekBar.setProgress(effectLevel.get(tabId));
            }
        });
        //不加这两行是无法在开启app的时候显示上面的图片和进度条的wtf...
        tabHost.setCurrentTab(1);
        tabHost.setCurrentTab(0);
    }

    private void initPopupWindow() {
        //设置点击图片弹出的菜单
        popupWindow = new PopupWindow();
        View popupView = getLayoutInflater().inflate(R.layout.pop_up, null);
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        Button camera = popupView.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "取消了拍照", Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap photo = data.getParcelableExtra("data");
            setDisplayImage(photo);
            popupWindow.dismiss();
        }
    }

    void setDisplayImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
