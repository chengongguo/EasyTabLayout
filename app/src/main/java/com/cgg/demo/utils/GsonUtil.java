package com.cgg.demo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class GsonUtil {
    public static String toString(Object object) {
        try {
            return new Gson().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = new Gson().fromJson(jsonString, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> getList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
