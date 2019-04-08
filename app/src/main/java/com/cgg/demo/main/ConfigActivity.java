package com.cgg.demo.main;

import com.cgg.demo.config.Config;
import com.cgg.demo.config.PathParser;
import com.cgg.demo.http.EasyHttp;
import com.cgg.demo.tab.BasePagerTabActivity;
import com.cgg.demo.tab.TabFragment;

import com.cgg.demo.tab.TabLayoutBean;
import com.cgg.demo.utils.GsonUtil;
import com.cgg.tablayout.Tab;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends BasePagerTabActivity {
    private static final String TAG = Config.TAG_PREFIX + ConfigActivity.class.getSimpleName();

    @Override
    protected void initContentView() {
        String fullPath = PathParser.getFullPath("tab/ConfigActivity.json");
        Log.i(TAG, "initContentView() fullPath=" + fullPath);
        EasyHttp.get(fullPath, new EasyHttp.RequestListener() {
            @Override
            public void onFail(String msg) {
                Log.i(TAG, "onFail() msg=" + msg);
            }

            @Override
            public void onSuccess(String data) {
                Log.i(TAG, "onSuccess() data=" + data);
                initPagerTab(data);
            }
        });
    }

    private void initPagerTab(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        List<TabLayoutBean> tabLayoutBeanList = GsonUtil.getList(data, TabLayoutBean.class);
        if (tabLayoutBeanList == null || tabLayoutBeanList.size() == 0) {
            return;
        }
        List<TabFragment> tabFragmentList = new ArrayList<>();
        for (TabLayoutBean tabLayoutBean : tabLayoutBeanList) {
            if (tabLayoutBean == null || tabLayoutBean.hide) {
                continue;
            }
            Tab tab = new Tab(tabLayoutBean.title, tabLayoutBean.unSelectedUrl, tabLayoutBean.selectedUrl);
            TabFragment tabFragment = new TabFragment(tab, new DefaultFragment());
            tabFragmentList.add(tabFragment);
        }
        initPagerTab(tabFragmentList, 2);
    }
}
