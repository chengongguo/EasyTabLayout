package com.cgg.demo.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class AssetsUtil {
    public static String getString(Context context, String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static <T> T getObjectFromAssets(Context context, String path, Class<T> cls) {
        String jsonString = getString(context, path);
        return GsonUtil.getObject(jsonString, cls);
    }

    public static <T> List<T> getListFromAssets(Context context, String path, Class<T> cls) {
        String jsonString = getString(context, path);
        return GsonUtil.getList(jsonString, cls);
    }
}
