package com.cgg.android.easypagertablayout;

import com.cgg.android.tablayout.Tab;

import androidx.fragment.app.Fragment;

public class TabFragment {
    public Tab tab;

    public Fragment fragment;

    public TabFragment(Tab tab, Fragment fragment) {
        this.tab = tab;
        this.fragment = fragment;
    }

}
