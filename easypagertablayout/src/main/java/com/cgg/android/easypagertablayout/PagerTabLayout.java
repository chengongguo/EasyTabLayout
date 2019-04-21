package com.cgg.android.easypagertablayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cgg.android.tablayout.EasyTabLayout;
import com.cgg.android.tablayout.Tab;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class PagerTabLayout extends LinearLayout {
    private static final String TAG = PagerTabLayout.class.getSimpleName();
    public static final int LAYOUT_TYPE_NO_SCROLL_PAGER_BOTTOM_TAB = 1;
    public static final int LAYOUT_TYPE_PAGER_BOTTOM_TAB = 2;
    public static final int LAYOUT_TYPE_PAGER_TOP_SCROLL_TAB = 3;
    private int mLayoutRes = R.layout.layout_pager_bottomtab;
    private EasyTabLayout.ImageListener mImageListener;
    private TabLayoutConfigListener mTabLayoutConfigListener;

    public interface TabLayoutConfigListener {
        void configTabLayout(EasyTabLayout easyTabLayout);
    }

    public PagerTabLayout(Context context) {
        super(context);
    }

    public PagerTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void configImageListener(EasyTabLayout.ImageListener imageListener) {
        mImageListener = imageListener;
    }

    public void configTabLayoutConfigListener(TabLayoutConfigListener tabLayoutConfigListener) {
        mTabLayoutConfigListener = tabLayoutConfigListener;
    }

    public void configCustomLayoutRes(@LayoutRes int layoutRes) {
        mLayoutRes = layoutRes;
    }

    public void configLayoutType(int layoutType) {
        switch (layoutType) {
            case LAYOUT_TYPE_NO_SCROLL_PAGER_BOTTOM_TAB:
                mLayoutRes = R.layout.layout_noscrollpager_bottomtab;
                break;
            case LAYOUT_TYPE_PAGER_BOTTOM_TAB:
                mLayoutRes = R.layout.layout_pager_bottomtab;
                break;
            case LAYOUT_TYPE_PAGER_TOP_SCROLL_TAB:
                mLayoutRes = R.layout.layout_pager_topscrolltab;
                break;
        }
    }

    public void init(FragmentManager fragmentManager, List<TabFragment> tabFragmentList, int selectedId) {
        Log.i(TAG, "init() " + tabFragmentList);
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
        init(fragmentManager, tabs, fragments, selectedId);
    }

    public void init(FragmentManager fragmentManager, List<Tab> tabs, List<Fragment> fragments, int selectedId) {
        Log.i(TAG, "init() tabs:" + tabs + " fragments:" + fragments);
        if (fragmentManager == null || tabs.size() == 0 || tabs.size() != fragments.size()) {
            return;
        }
        if (mLayoutRes == 0) {
            Log.e(TAG, "layout res is error.");
            return;
        }
        View view = LayoutInflater.from(getContext()).inflate(mLayoutRes, this);
        ViewGroup viewGroup = view.findViewById(R.id.easy_TabLayout);
        final ViewPager viewPager = view.findViewById(R.id.easy_ViewPager);
        BasePagerAdapter basePagerAdapter = new BasePagerAdapter(fragmentManager, fragments, Util.getTitles(tabs));
        viewPager.setAdapter(basePagerAdapter);
        if (viewGroup instanceof TabLayout) {
            TabLayout tabLayout = (TabLayout) viewGroup;
            tabLayout.setupWithViewPager(viewPager);
        } else if (viewGroup instanceof EasyTabLayout) {
            final EasyTabLayout easyTabLayout = (EasyTabLayout) viewGroup;
            if (tabs.size() == 1) {
                easyTabLayout.setVisibility(View.GONE);
            }
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    easyTabLayout.selectTab(position);
                }
            });

            if (mImageListener != null) {
                easyTabLayout.setImageListener(mImageListener);
            }
            if (mTabLayoutConfigListener != null) {
                mTabLayoutConfigListener.configTabLayout(easyTabLayout);
            }
            easyTabLayout.addTabListener(new EasyTabLayout.TabListener() {
                @Override
                public void onTabSelected(int selectedId) {
                    viewPager.setCurrentItem(selectedId);
                }

                @Override
                public void onTabReSelected(int selectedId) {

                }
            });
            easyTabLayout.init(tabs, selectedId);
        }
    }
}
