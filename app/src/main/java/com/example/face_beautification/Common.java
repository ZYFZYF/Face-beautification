package com.example.face_beautification;

import java.util.HashMap;

public class Common {
    final static String[] EFFECT_SET = {"Whitening", "Smoother"};
    final static HashMap<String, String> EFFECT_SHOW_NAME = new HashMap<String, String>() {{
        put("Whitening", "美白");
        put("Smoother", "磨皮");
    }};
}
