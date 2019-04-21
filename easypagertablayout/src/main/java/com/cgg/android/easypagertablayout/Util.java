package com.cgg.android.easypagertablayout;

import com.cgg.android.tablayout.Tab;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<String> getTitles(List<Tab> tabs) {
        if (tabs == null) {
            return null;
        }
        List<String> titles = new ArrayList<>();
        for (Tab tab : tabs) {
            titles.add(tab.title);
        }
        return titles;
    }
}
