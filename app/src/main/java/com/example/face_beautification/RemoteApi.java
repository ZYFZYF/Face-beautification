package com.example.face_beautification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RemoteApi {
    private static final String THOUSAND_LANDMARK_API = "https://api-cn.faceplusplus.com/facepp/v1/face/thousandlandmark";
    private static final String API_KEY = "Jcner27-x9ZxpYIV3vKh7xhvZLwpzh5b";
    private static final String API_SECRET = "auzWW7ZbB4dEE10wOjSq1cZ83jFH8j2g";
    private static final String TEST_IMAGE_FILE = "/Users/zhaoyunfeng/Desktop/大四上/图像处理/作业/Face-beautification/app/src/main/res/drawable/test.jpg";

    //避免用Android函数，方便调试
    public static String getFaceLandmarks(final String base64Img) {
        HashMap<String, String> paramMap = new HashMap<String, String>() {{
            put("api_key", API_KEY);
            put("api_secret", API_SECRET);
            put("return_landmark", "all");
            put("image_base64", base64Img);
            //put("image_file", "@" + TEST_IMAGE_FILE);
        }};
        return HttpPostUrl.sendPost(THOUSAND_LANDMARK_API, paramMap);
    }

    public static String getFaceLandmarks(Bitmap bitmap) {
        return getFaceLandmarks(bitmapToBase64(bitmap));
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                //result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                result = Base64.getEncoder().encodeToString(bitmapBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.getDecoder().decode(base64Data);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * 本地图片转换成base64字符串
     *
     * @param imgFile 图片本地路径
     * @return
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:40:46
     */
    public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.getEncoder().encodeToString(data);
        //return  Base64.encodeToString(data, Base64.DEFAULT);// 返回Base64编码过的字节数组字符串
    }


    public static void main() {
        //Bitmap bitmap = BitmapFactory.decodeFile("test.jpg");
        System.out.println(getFaceLandmarks(ImageToBase64ByLocal(TEST_IMAGE_FILE)));
    }
}

class HttpPostUrl {

    /**
     * 发送HttpPost请求
     *
     * @param strURL 服务地址
     * @param params
     * @return 成功:返回json字符串<br/>
     */
    public static String sendPost(String strURL, Map<String, String> params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(strURL)
                .addHeader("User-Agent", "OkHttp Bot")
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            //System.out.println(response.body().string());
            return response.body().string();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}