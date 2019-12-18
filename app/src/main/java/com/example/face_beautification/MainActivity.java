package com.example.face_beautification;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {
//    static {
//        System.loadLibrary("opencv_java");
//    }

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    final int TAKE_PHOTO_REQUEST = 0;
    final int PICK_PHOTO_IN_ALBUM = 1;
    final int TRY_LIMIT = 3;
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
    private PopupWindow operateWindow;
    private PopupWindow loadingWindow;
    private String photoPath;
    private Bitmap readyBitmap;
    private PictureManager readyPictureManager;
    private FaceLandmark faceLandmark;
    private Handler handler;
    private LinearLayout linearLayout;
    private boolean isGettingFaceLandmarks = false;
    private boolean readyToMakeUp = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isGettingFaceLandmarks) {
            return super.dispatchTouchEvent(ev);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        initPopupWindow();
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_12);
        initImageView(bitmap);
        initLevel();
        initSeekBar();
        initTabHost();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        linearLayout = findViewById(R.id.linearLayout);
        try {
            InputStream is = getResources().openRawResource(R.raw.test_11);
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            String defaultImageResponse = new String(buffer);
            System.out.println(defaultImageResponse);
            JSONObject jsonObject = new JSONObject(new JSONTokener(defaultImageResponse));
            System.out.println(jsonObject);
            faceLandmark = new FaceLandmark(jsonObject);
            pictureManager = new PictureManager(bitmap, faceLandmark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImageView(Bitmap bitmap) {
        //设置显示的默认图片，以及点击回调
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateWindow.showAtLocation(findViewById(R.id.linearLayout), Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private void initLevel() {

        nowEffect = "Whitening";
        effectLevel = new HashMap<>();
        //初始化各个美颜效果对应的level
        for (String effect : Common.EFFECT_SET) {
            effectLevel.put(effect, 0);
        }

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
        operateWindow = new PopupWindow();
        View popupView = getLayoutInflater().inflate(R.layout.pop_up, null);
        operateWindow.setContentView(popupView);
        operateWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        operateWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        operateWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        operateWindow.setFocusable(true);
        operateWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        //设置拍照的点击
        Button camera = popupView.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoPath = generatePhotoPath();
                System.out.println(photoPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        });
        //设置从相册选取的点击
        Button album = popupView.findViewById(R.id.album);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PHOTO_IN_ALBUM);
            }
        });
        //设置获取人脸关键点时的loading画面
        loadingWindow = new PopupWindow();
        View loadingView = getLayoutInflater().inflate(R.layout.loading, null);
        loadingWindow.setContentView(loadingView);
        loadingWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        loadingWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingWindow.setAnimationStyle(R.style.anim_menu_bottombar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "取消了拍照", Toast.LENGTH_LONG).show();
                return;
            }
            //Bitmap photo = data.getParcelableExtra("data");
            Bitmap photo = BitmapFactory.decodeFile(photoPath);
            replaceImage(photo);
            operateWindow.dismiss();
        }
        if (requestCode == PICK_PHOTO_IN_ALBUM) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                return;
            }
            Uri uri = data.getData();
            String path = uri2path(uri);
            System.out.println(path);
            Bitmap photo = BitmapFactory.decodeFile(uri2path(uri));
            replaceImage(photo);
            operateWindow.dismiss();
        }
    }

    void replaceImage(Bitmap bitmap) {
        //先保存下拍照/相册里的图片
        readyBitmap = bitmap;
        //背景虚化
        linearLayout.setAlpha(0.5f);
        //背景不可操作
        isGettingFaceLandmarks = true;
        //出现加载动画，这里用异步是因为有可能一开始就会调用这个函数
        loadingWindow.showAtLocation(findViewById(R.id.linearLayout), Gravity.CENTER, 0, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //如果出现异常，说明没get到正确的信息，再跑一遍就好了
                int try_count;
                for (try_count = 0; try_count < TRY_LIMIT; try_count++) {
                    String response = RemoteApi.getFaceLandmarks(readyBitmap);
                    try {
                        JSONObject jsonObject = new JSONObject(new JSONTokener(response));
                        System.out.println(jsonObject);
                        faceLandmark = new FaceLandmark(jsonObject);
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (try_count < TRY_LIMIT) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            readyToMakeUp = true;
                            //重置所有效果的level
                            for (String effect : Common.EFFECT_SET) {
                                effectLevel.put(effect, 0);
                            }
                            //让背景可点击
                            isGettingFaceLandmarks = false;
                            //恢复背景原色
                            linearLayout.setAlpha(1.0f);
                            //显示新图片
                            imageView.setImageBitmap(readyBitmap);
                            //新建pictureManager
                            pictureManager = new PictureManager(readyBitmap, faceLandmark);
                            //提示
                            Toast.makeText(MainActivity.this, "获取人脸关键点成功๑乛◡乛๑", Toast.LENGTH_LONG).show();
                            //去除加载动画
                            loadingWindow.dismiss();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            isGettingFaceLandmarks = false;
                            linearLayout.setAlpha(1.0f);
                            Toast.makeText(MainActivity.this, "获取人脸关键点失败(>﹏<)\n 请检查网络连接或者重启APP或者换一张图片┐(─__─)┌", Toast.LENGTH_LONG).show();
                            loadingWindow.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    private String generatePhotoPath() {
        return getExternalFilesDir("images") + "/" + System.currentTimeMillis() + ".jpg";
    }

    //fit <=4.4
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }

    // fit all
    private String uri2path(Uri uri) {
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                return getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                return getImagePath(contentUri, null);
            } else {
                return null;
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            return getImagePath(uri, null);
        } else {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

}
