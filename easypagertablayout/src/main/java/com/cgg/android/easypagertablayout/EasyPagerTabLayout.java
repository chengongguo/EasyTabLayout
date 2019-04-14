package com.cgg.android.easypagertablayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.cgg.android.tablayout.EasyTabLayout;
import com.cgg.android.tablayout.Tab;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class EasyPagerTabLayout extends PagerTabLayout {
    private static final String TAG = EasyPagerTabLayout.class.getSimpleName();

    public EasyPagerTabLayout(Context context) {
        super(context);
    }

    public EasyPagerTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyPagerTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initPagerTab(FragmentManager fragmentManager, List<TabFragment> tabFragmentList,
                             int selectedId,
                             EasyTabLayout.ImageListener imageListener) {
        if (fragmentManager == null || tabFragmentList == null || tabFragmentList.size() == 0) {
            return;
        }
        List<Tab> tabs = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for (TabFragment tabFragment : tabFragmentList) {
            if (tabFragment.tab == null || tabFragment.fragment == null) {
                continue;
            }
            tabs.add(tabFragment.tab);
            if (!TextUtils.isEmpty(tabFragment.tab.title)) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("tabTitle", tabFragment.tab.title);
                tabFragment.fragment.setArguments(bundle1);
            }
            fragments.add(tabFragment.fragment);
        }
        Log.i(TAG, "init() " + tabs.toString());
        initPagerTab(fragmentManager, tabs, fragments, LAYOUT_TYPE_PAGER_BOTTOM_TAB, selectedId, null);
    }

    public void initPagerTab(FragmentManager fragmentManager, List<TabBean> tabBeanList, int selectedId,
                             ProviderFragmentListener providerFragmentListener, EasyTabLayout.ImageListener imageListener) {
        if (fragmentManager == null || providerFragmentListener == null) {
            return;
        }
        if (tabBeanList == null || tabBeanList.size() == 0) {
            return;
        }
        List<Tab> tabs = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for (TabBean tabBean : tabBeanList) {
            if (tabBean == null || tabBean.hide) {
                continue;
            }
            Tab tab = new Tab(tabBean.title, tabBean.unSelectedUrl, tabBean.selectedUrl);
            tabs.add(tab);
            Fragment fragment = providerFragmentListener.providerFragment(tabBean.name, tab.title);
            Bundle bundle1 = new Bundle();
            if (!TextUtils.isEmpty(tabBean.name)) {
                bundle1.putString("tabName", tabBean.name);
            }
            if (!TextUtils.isEmpty(tab.title)) {
                bundle1.putString("tabTitle", tab.title);
            }
            fragment.setArguments(bundle1);
            fragments.add(fragment);
        }
        if (tabs.size() == 0 || tabs.size() != fragments.size()) {
            return;
        }
        initPagerTab(fragmentManager, tabs, fragments, LAYOUT_TYPE_PAGER_BOTTOM_TAB, selectedId, imageListener);
    }

}
