package com.cgg.demo.main;

import com.cgg.demo.R;
import com.cgg.demo.tab.BasePagerTabActivity;
import com.cgg.demo.tab.TabFragment;
import com.cgg.tablayout.Tab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasePagerTabActivity {

    @Override
    protected void initContentView() {
        List<TabFragment> tabFragmentList = new ArrayList<>();
        tabFragmentList.add(new TabFragment(new Tab("首页", R.drawable.tab_home_normal, R.drawable.tab_home_selected), new DefaultFragment()));
        tabFragmentList.add(new TabFragment(new Tab("微头条", R.drawable.tab_micro_normal, R.drawable.tab_micro_selected), new DefaultFragment()));
        tabFragmentList.add(new TabFragment(new Tab("视频", R.drawable.tab_video_normal, R.drawable.tab_video_selected), new DefaultFragment()));
        tabFragmentList.add(new TabFragment(new Tab("我的", R.drawable.tab_me_normal, R.drawable.tab_me_selected), new DefaultFragment()));
        initPagerTab(tabFragmentList, 2);
    }
}
