package com.cgg.demo.main;

import com.cgg.android.easypagertablayout.EasyPagerTabLayout;
import com.cgg.android.easypagertablayout.TabFragment;
import com.cgg.android.tablayout.Tab;
import com.cgg.demo.R;
import com.cgg.demo.base.BaseButterKnifeActivity;
import com.cgg.demo.config.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseButterKnifeActivity {
    private static final String TAG = Config.TAG_PREFIX + MainActivity.class.getSimpleName();
    @BindView(R.id.easyPagerBottomTabLayout)
    EasyPagerTabLayout easyPagerBottomTabLayout;

    @Override
    protected int provideContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initContentView() {
        List<TabFragment> tabFragmentList = new ArrayList<>();
        tabFragmentList.add(new TabFragment(new Tab("首页", R.drawable.tab_home_normal, R.drawable.tab_home_selected), new DefaultFragment()));
        tabFragmentList.add(new TabFragment(new Tab("微头条", R.drawable.tab_micro_normal, R.drawable.tab_micro_selected), new DefaultFragment()));
        tabFragmentList.add(new TabFragment(new Tab("视频", R.drawable.tab_video_normal, R.drawable.tab_video_selected), new DefaultFragment()));
        tabFragmentList.add(new TabFragment(new Tab("我的", R.drawable.tab_me_normal, R.drawable.tab_me_selected), new DefaultFragment()));
        easyPagerBottomTabLayout.initPagerTab(getSupportFragmentManager(), tabFragmentList, 2, null);
    }

}
