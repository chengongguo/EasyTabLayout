package com.cgg.android.easypagertablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cgg.android.tablayout.EasyTabLayout;
import com.cgg.android.tablayout.Tab;

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
    public static final int LAYOUT_TYPE_NO_SCROLL_PAGER_TOP_SCROLL_TAB = 3;
    public static final int LAYOUT_TYPE_PAGER_TOP_TAB = 4;

    public interface ProviderFragmentListener {
        Fragment providerFragment(String tabName, String tabTitle);
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

    public void initCustomPagerTab(FragmentManager fragmentManager, List<Tab> tabs, List<Fragment> fragments,
                                   @LayoutRes int layoutRes, int selectedId,
                                   EasyTabLayout.ImageListener imageListener) {
        init(fragmentManager, tabs, fragments, layoutRes, selectedId, imageListener);
    }

    public void initPagerTab(FragmentManager fragmentManager, List<Tab> tabs, List<Fragment> fragments,
                             int layoutType, int selectedId,
                             EasyTabLayout.ImageListener imageListener) {
        int layoutRes = 0;
        switch (layoutType) {
            case LAYOUT_TYPE_NO_SCROLL_PAGER_BOTTOM_TAB:
                layoutRes = R.layout.layout_noscrollpager_bottomtab;
                break;
            case LAYOUT_TYPE_PAGER_BOTTOM_TAB:
                layoutRes = R.layout.layout_pager_bottomtab;
                break;
            case LAYOUT_TYPE_NO_SCROLL_PAGER_TOP_SCROLL_TAB:
                layoutRes = R.layout.layout_pager_topscrolltab;
                break;
            case LAYOUT_TYPE_PAGER_TOP_TAB:
                layoutRes = R.layout.layout_pager_toptab;
                break;
        }
        init(fragmentManager, tabs, fragments, layoutRes, selectedId, imageListener);
    }

    private void init(FragmentManager fragmentManager, List<Tab> tabs, List<Fragment> fragments,
                      @LayoutRes int layoutRes, int selectedId,
                      EasyTabLayout.ImageListener imageListener) {
        Log.i(TAG, "init() " + tabs);
        if (fragmentManager == null || tabs.size() == 0 || tabs.size() != fragments.size()) {
            return;
        }
        View view = LayoutInflater.from(getContext()).inflate(layoutRes, this);
        final ViewPager viewPager = view.findViewById(R.id.easy_ViewPager);
        final EasyTabLayout easyTabLayout = view.findViewById(R.id.easy_TabLayout);
        if (tabs.size() == 1) {
            easyTabLayout.setVisibility(View.GONE);
        }
        BasePagerAdapter basePagerAdapter = new BasePagerAdapter(fragmentManager, fragments);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                easyTabLayout.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(basePagerAdapter);
        easyTabLayout.setImageListener(imageListener);
        easyTabLayout.setTabListener(new EasyTabLayout.TabListener() {
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
