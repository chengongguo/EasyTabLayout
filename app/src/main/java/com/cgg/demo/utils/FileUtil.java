package com.cgg.demo.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 使用LineNumberReader读取文件，1000w行比RandomAccessFile效率高，无法处理1亿条数据
     *
     * @param file     源文件
     * @param encoding 文件编码
     * @param index    开始位置
     * @param num      读取量
     * @return pins文件内容
     */
    public static List<String> readLine(File file, String encoding, int index, int num) {
        List<String> list = new ArrayList<>();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), encoding));
            int lines = 0;
            while (true) {
                String pin = reader.readLine();
                if (TextUtils.isEmpty(pin)) {
                    break;
                }
                if (lines >= index) {
                    list.add(pin);
                }
                if (num == list.size()) {
                    break;
                }
                lines++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
