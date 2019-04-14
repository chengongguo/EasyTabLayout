package com.cgg.android.tablayout;

public class Tab {
    public int id = -1;
    public int selectedIcon;
    public int unSelectedIcon;
    public String selectedUrl;
    public String unSelectedUrl;
    public String title;

    public Tab(String title, int unSelectedIcon, int selectedIcon) {
        this.title = title;
        this.unSelectedIcon = unSelectedIcon;
        this.selectedIcon = selectedIcon;
    }

    public Tab(String title, String unSelectedUrl, String selectedUrl) {
        this.title = title;
        this.unSelectedUrl = unSelectedUrl;
        this.selectedUrl = selectedUrl;
    }
}
