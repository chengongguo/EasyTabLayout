package com.cgg.demo.tab;

import com.cgg.tablayout.Tab;

import androidx.fragment.app.Fragment;

public class TabFragment {
    public Tab tab;

    public Fragment fragment;

    public TabFragment(Tab tab, Fragment fragment) {
        this.tab = tab;
        this.fragment = fragment;
    }

}
