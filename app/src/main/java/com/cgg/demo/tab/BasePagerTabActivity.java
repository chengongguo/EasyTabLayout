package com.cgg.demo.tab;

import com.cgg.demo.ImageLoader.ImageLoader;
import com.cgg.demo.R;
import com.cgg.demo.base.BaseApplication;
import com.cgg.demo.base.BaseButterKnifeActivity;
import com.cgg.demo.base.BasePagerAdapter;
import com.cgg.demo.config.Config;
import com.cgg.demo.config.PathParser;
import com.cgg.tablayout.EasyTabLayout;
import com.cgg.tablayout.Tab;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public abstract class BasePagerTabActivity extends BaseButterKnifeActivity {
    private static final String TAG = Config.TAG_PREFIX + BasePagerTabActivity.class.getSimpleName();

    @BindView(R.id.mainViewPager)
    protected ViewPager mMainViewPager;
    @BindView(R.id.easyTabLayout)
    protected EasyTabLayout mEasyTabLayout;

    protected int provideContentView() {
        return R.layout.layout_pager_bottomtab;
    }

    public void initPagerTab(List<TabFragment> tabFragmentList, int selectedId) {
        if (tabFragmentList == null || tabFragmentList.size() == 0) {
            return;
        }
        List<Tab> tabs = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for (TabFragment tabFragment : tabFragmentList) {
            if (tabFragment.tab == null || tabFragment.fragment == null) {
                continue;
            }
            tabs.add(tabFragment.tab);
            Bundle bundle1 = new Bundle();
            if (!TextUtils.isEmpty(tabFragment.tab.name)) {
                bundle1.putString("tabName", tabFragment.tab.name);
            }
            if (!TextUtils.isEmpty(tabFragment.tab.title)) {
                bundle1.putString("tabTitle", tabFragment.tab.title);
            }
            tabFragment.fragment.setArguments(bundle1);
            fragments.add(tabFragment.fragment);
        }
        Log.i(TAG, "initPagerTab() " + tabs.toString());
        if (tabFragmentList.size() == 1) {
            mEasyTabLayout.setVisibility(View.GONE);
        }
        BasePagerAdapter basePagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments);
        mMainViewPager.setAdapter(basePagerAdapter);
        initListener();
        mEasyTabLayout.init(tabs, selectedId);
    }

    private void initListener() {
        mEasyTabLayout.setImageListener(new EasyTabLayout.ImageListener() {
            @Override
            public void setImageUrl(ImageView imageView, String url, int iconRes) {
                String fullPath = PathParser.getFullPath(url);
                new ImageLoader(BaseApplication.sContext).setImageUrl(imageView, fullPath, iconRes);
            }
        });
        mEasyTabLayout.setTabListener(new EasyTabLayout.TabListener() {
            @Override
            public void onTabSelected(int selectedId) {
                mMainViewPager.setCurrentItem(selectedId);
            }

            @Override
            public void onTabReSelected(int selectedId) {

            }
        });
    }
}
