package com.meetup.backend.util.converter;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;

/**
 * created by seongmin on 2022/10/23
 */
public class JsonConverter {

    public static JSONObject toJson(BufferedInputStream bis) {
        JSONTokener tokener = new JSONTokener(bis);
        return new JSONObject(tokener);
    }
}
