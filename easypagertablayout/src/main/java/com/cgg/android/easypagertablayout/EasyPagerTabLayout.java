package com.cgg.android.easypagertablayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.cgg.android.tablayout.Tab;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class EasyPagerTabLayout extends PagerTabLayout {
    private static final String TAG = EasyPagerTabLayout.class.getSimpleName();
    private ProviderFragmentListener mProviderFragmentListener;

    public interface ProviderFragmentListener {
        Fragment providerFragment(String tabName, String tabTitle);
    }

    public EasyPagerTabLayout(Context context) {
        super(context);
    }

    public EasyPagerTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyPagerTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProviderFragmentListener(ProviderFragmentListener providerFragmentListener) {
        this.mProviderFragmentListener = providerFragmentListener;
    }

    public void initPagerTab(FragmentManager fragmentManager, List<TabBean> tabBeanList, int selectedId) {
        if (fragmentManager == null) {
            return;
        }
        if (mProviderFragmentListener == null) {
            Log.e(TAG, "you has not set fragment.");
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
            Fragment fragment = mProviderFragmentListener.providerFragment(tabBean.name, tab.title);
            if (fragment == null) {
                continue;
            }
            tabs.add(tab);
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
        init(fragmentManager, tabs, fragments, selectedId);
    }

}
