package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("opencv_java");
    }

    private ImageView imageView;
    private FaceLandmark faceLandmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        imageView.setImageBitmap(bitmap);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurImage(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        });

    }

    private void blurImage(Bitmap origin) {
        Mat mat = new Mat();
        Utils.bitmapToMat(origin, mat);
        Imgproc.GaussianBlur(mat, mat, new Size(15, 15), 0);
        Utils.matToBitmap(mat, origin);
    }
}
