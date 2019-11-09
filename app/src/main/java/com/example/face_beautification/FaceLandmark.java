package com.example.face_beautification;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FaceLandmark {
    private final String TAG = "FACELANDMARK";
    int leftEyePupleRadius;
    int rightEyePupleRadius;
    Point leftEyePupleCenter;
    Point rightEyePupleCenter;
    Point leftNostril, rightNostril;
    private Point faceLeftTop, faceSize;
    private List<Point> faceContour, faceHairline, faceContourRight, faceContourLeft;
    private List<Point> leftEyebrow, leftEye, leftEyeEyelid;
    private List<Point> rightEyebrow, rightEye, rightEyeEyelid;
    private List<Point> nose, noseLeft, noseRight, noseMidline;
    private List<Point> upperLip, lowerLip;

    FaceLandmark(JSONObject object) {
        try {
            //关键点表示方法参照https://console.faceplusplus.com.cn/documents/55107022
            JSONObject faceObject = object.getJSONObject("face");
            //拿到人脸的长方体轮廓
            JSONObject faceRectangle = faceObject.getJSONObject("face_rectangle");
            faceLeftTop = new Point(faceRectangle.getInt("left"), faceRectangle.getInt("top"));
            faceSize = new Point(faceRectangle.getInt("width"), faceRectangle.getInt("height"));
            System.out.println(faceLeftTop.toString() + faceSize.toString());
            //拿关键点轮廓
            JSONObject landmark = faceObject.getJSONObject("landmark");
            //拿脸的关键点轮廓
            JSONObject face = landmark.getJSONObject("face");
            faceHairline = generateOrderedPrefixData(face, "face_hairline");
            //这两部分都是从下巴开始到耳朵
            faceContourLeft = generateOrderedPrefixData(face, "face_contour_left");
            faceContourRight = generateOrderedPrefixData(face, "face_contour_right");
            //翻转为了让轮廓整体从左到右
            faceContour = new ArrayList<>();
            Collections.reverse(faceContourLeft);
            faceContour.addAll(faceContourLeft);
            Collections.reverse(faceContourLeft);
            faceContour.addAll(faceContourRight);
            //拿左眼的相关轮廓
            leftEye = generateOrderedPrefixData(landmark.getJSONObject("left_eye"), "left_eye");
            leftEyebrow = generateOrderedPrefixData(landmark.getJSONObject("left_eyebrow"), "left_eyebrow");
            leftEyeEyelid = generateOrderedPrefixData(landmark.getJSONObject("left_eye_eyelid"), "left_eye_eyelid");
            leftEyePupleCenter = convertObjectToPoint(landmark.getJSONObject("left_eye").getJSONObject("left_eye_pupil_center"));
            leftEyePupleRadius = landmark.getJSONObject("left_eye").getInt("left_eye_pupil_radius");
            //拿右眼的相关轮廓
            rightEye = generateOrderedPrefixData(landmark.getJSONObject("right_eye"), "right_eye");
            rightEyebrow = generateOrderedPrefixData(landmark.getJSONObject("right_eyebrow"), "right_eyebrow");
            rightEyeEyelid = generateOrderedPrefixData(landmark.getJSONObject("right_eye_eyelid"), "right_eye_eyelid");
            rightEyePupleCenter = convertObjectToPoint(landmark.getJSONObject("right_eye").getJSONObject("right_eye_pupil_center"));
            rightEyePupleRadius = landmark.getJSONObject("right_eye").getInt("right_eye_pupil_radius");
            //拿鼻子相关轮廓
            JSONObject noseObject = landmark.getJSONObject("nose");
            //这两部分都是从上方到两边
            noseLeft = generateOrderedPrefixData(noseObject, "nose_left");
            noseRight = generateOrderedPrefixData(noseObject, "nose_right");
            noseMidline = generateOrderedPrefixData(noseObject, "nose_midline");
            nose = new ArrayList<>();
            nose.addAll(noseLeft);
            Collections.reverse(noseRight);
            nose.addAll(noseLeft);
            Collections.reverse(noseRight);
            leftNostril = convertObjectToPoint(noseObject.getJSONObject("left_nostril"));
            rightNostril = convertObjectToPoint(noseObject.getJSONObject("right_nostril"));
            //拿嘴唇轮廓,都是从外到内，从左到右再从右到左
            JSONObject mouthObject = landmark.getJSONObject("mouth");
            upperLip = generateOrderedPrefixData(mouthObject, "upper_lip");
            lowerLip = generateOrderedPrefixData(mouthObject, "lower_lip");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "can't initialize facelandmark using return object");
        }
    }

    public int getLeftEyePupleRadius() {
        return leftEyePupleRadius;
    }

    public int getRightEyePupleRadius() {
        return rightEyePupleRadius;
    }

    public Point getLeftEyePupleCenter() {
        return leftEyePupleCenter;
    }

    public Point getRightEyePupleCenter() {
        return rightEyePupleCenter;
    }

    public Point getLeftNostril() {
        return leftNostril;
    }

    public Point getRightNostril() {
        return rightNostril;
    }

    public Point getFaceLeftTop() {
        return faceLeftTop;
    }

    public Point getFaceSize() {
        return faceSize;
    }

    public List<Point> getFaceContour() {
        return faceContour;
    }

    public List<Point> getFaceHairline() {
        return faceHairline;
    }

    public List<Point> getFaceContourRight() {
        return faceContourRight;
    }

    public List<Point> getFaceContourLeft() {
        return faceContourLeft;
    }

    public List<Point> getLeftEyebrow() {
        return leftEyebrow;
    }

    public List<Point> getLeftEye() {
        return leftEye;
    }

    public List<Point> getLeftEyeEyelid() {
        return leftEyeEyelid;
    }

    public List<Point> getRightEyebrow() {
        return rightEyebrow;
    }

    public List<Point> getRightEye() {
        return rightEye;
    }

    public List<Point> getRightEyeEyelid() {
        return rightEyeEyelid;
    }

    public List<Point> getNose() {
        return nose;
    }

    public List<Point> getNoseLeft() {
        return noseLeft;
    }

    public List<Point> getNoseRight() {
        return noseRight;
    }

    public List<Point> getNoseMidline() {
        return noseMidline;
    }

    public List<Point> getUpperLip() {
        return upperLip;
    }

    public List<Point> getLowerLip() {
        return lowerLip;
    }

    //从一个JSONObject中获取所有前缀为指定前缀的有序的List<Point>
    private List<Point> generateOrderedPrefixData(JSONObject object, String prefix) throws JSONException {
        int count = 0;
        for (Iterator<String> keys = object.keys(); keys.hasNext(); ) {
            String key = keys.next();
            if (key.startsWith(prefix) && isNumber(key.substring(prefix.length() + 1))) {
                count++;
            }
        }
        List<Point> list = new ArrayList<>(Arrays.asList(new Point[count]));
        for (Iterator<String> keys = object.keys(); keys.hasNext(); ) {
            String key = keys.next();
            if (key.startsWith(prefix) && isNumber(key.substring(prefix.length() + 1))) {
                int index = Integer.valueOf(key.substring(prefix.length() + 1));
                JSONObject value = object.getJSONObject(key);
                list.set(index, convertObjectToPoint(value));
            }
        }
        Log.i(TAG, "prefix " + prefix + " get " + list.size() + " elements");
        return list;
    }

    //判断一个String是不是纯数字
    private boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //把JsonObject转成point
    private Point convertObjectToPoint(JSONObject object) throws JSONException {
        return new Point(object.getInt("x"), object.getInt("y"));
    }
}
