package com.esaa.corp.commons.utils;

import com.google.gson.Gson;

public class JsonParser {

    private static JsonParser INSTANCE;
    private final Gson gson = new Gson();
    private JsonParser() {

    }

    public static JsonParser getInstance() {
        if(INSTANCE == null){
            INSTANCE = new JsonParser();
        }

        return INSTANCE;
    }

    public  <T> T fromJson(String json, Class<T> clazz) {
       return gson.fromJson(json, clazz);
    }
}
