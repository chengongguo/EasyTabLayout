package com.cgg.android.easypagertablayout;

import com.google.gson.annotations.SerializedName;

public class TabBean {

    /**
     * name : home
     * title : 首页
     * unSelectedUrl : icon/tab/tab_home_normal.png
     * selectedUrl : icon/tab/tab_home_selected.png
     * hide : false
     */

    @SerializedName("name")
    public String name;
    @SerializedName("title")
    public String title;
    @SerializedName("unSelectedUrl")
    public String unSelectedUrl;
    @SerializedName("selectedUrl")
    public String selectedUrl;
    @SerializedName("hide")
    public boolean hide;
}
