package com.cgg.demo.main;

import com.cgg.android.easypagertablayout.PagerTabLayout;
import com.cgg.android.easypagertablayout.TabFragment;
import com.cgg.android.tablayout.EasyTabLayout;
import com.cgg.android.tablayout.Tab;
import com.cgg.demo.R;
import com.cgg.demo.base.BaseButterKnifeActivity;
import com.cgg.demo.config.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseButterKnifeActivity {
    private static final String TAG = Config.TAG_PREFIX + MainActivity.class.getSimpleName();
    @BindView(R.id.pagerBottomTabLayout)
    PagerTabLayout pagerTabLayout;

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
//                tabFragmentList.add(new TabFragment(new Tab("首页"), new DefaultFragment()));
//                tabFragmentList.add(new TabFragment(new Tab("微头条"), new DefaultFragment()));
//                tabFragmentList.add(new TabFragment(new Tab("视频"), new DefaultFragment()));
//                tabFragmentList.add(new TabFragment(new Tab("我的"), new DefaultFragment()));
//                pagerTabLayout.setLayoutType(PagerTabLayout.LAYOUT_TYPE_PAGER_BOTTOM_TAB);
//                pagerTabLayout.setLayoutType(PagerTabLayout.LAYOUT_TYPE_PAGER_LEFT_SCROLL_TAB);
        pagerTabLayout.setLayoutType(PagerTabLayout.LAYOUT_TYPE_PAGER_TOP_SCROLL_TAB);
        pagerTabLayout.setTabLayoutConfigListener(new PagerTabLayout.TabLayoutConfigListener() {
            @Override
            public void configTabLayout(EasyTabLayout easyTabLayout) {
                easyTabLayout.configBackgroundColor("#100000dd");
                easyTabLayout.configItemPadding(5, 20, 5, 20);
            }
        });
        pagerTabLayout.init(getSupportFragmentManager(), tabFragmentList, 2);
    }

}
