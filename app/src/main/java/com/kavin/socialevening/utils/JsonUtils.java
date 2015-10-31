package com.kavin.socialevening.utils;

import com.google.gson.Gson;
import com.kavin.socialevening.server.dto.PushDto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 01/11/15
 * Author     : Kavin Varnan
 */
public class JsonUtils {
    /**
     * Convert an object to JSON String
     * @param object the object
     * @return String value of the Object in JSON format
     */
    public static String convertObjectToJson(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Convert the respective JSON String to an object
     * @param json String in JSON format
     * @param whichClass the modal class itself
     * @return the model object with data
     */
    public static Object convertJsonStringToObject(String json, Class<?> whichClass) {
        return  new Gson().fromJson(json, whichClass);
    }

    public static JSONObject convertObjectToJSONObject(PushDto pushDto) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", pushDto.getMessage());
            jsonObject.put("pushType", pushDto.getPushType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
