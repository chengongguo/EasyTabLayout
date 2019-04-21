package com.cgg.demo.main;

import android.util.Log;
import android.widget.ImageView;

import com.cgg.android.easypagertablayout.EasyPagerTabLayout;
import com.cgg.android.easypagertablayout.TabBean;
import com.cgg.android.tablayout.EasyTabLayout;
import com.cgg.demo.ImageLoader.ImageLoader;
import com.cgg.demo.R;
import com.cgg.demo.base.BaseApplication;
import com.cgg.demo.base.BaseButterKnifeActivity;
import com.cgg.demo.config.Config;
import com.cgg.demo.config.PathParser;
import com.cgg.demo.http.EasyHttp;
import com.cgg.demo.utils.GsonUtil;

import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;

public class ConfigActivity extends BaseButterKnifeActivity {
    private static final String TAG = Config.TAG_PREFIX + ConfigActivity.class.getSimpleName();
    @BindView(R.id.pagerBottomTabLayout)
    EasyPagerTabLayout easyPagerBottomTabLayout;

    @Override
    protected int provideContentView() {
        return R.layout.activity_config;
    }

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
                List<TabBean> tabBeanList = GsonUtil.getList(data, TabBean.class);
                initPagerTab(tabBeanList);
            }
        });
    }

    private void initPagerTab(List<TabBean> tabBeanList) {
        if (tabBeanList == null || tabBeanList.size() == 0) {
            return;
        }
        easyPagerBottomTabLayout.initPagerTab(getSupportFragmentManager(), tabBeanList, 2);
        easyPagerBottomTabLayout.setProviderFragmentListener(new EasyPagerTabLayout.ProviderFragmentListener() {
            @Override
            public Fragment providerFragment(String tabName, String tabTitle) {
                return new DefaultFragment();
            }
        });
        easyPagerBottomTabLayout.configImageListener(new EasyTabLayout.ImageListener() {
            @Override
            public void setImageUrl(ImageView imageView, String url, int iconRes) {
                String fullPath = PathParser.getFullPath(url);
                new ImageLoader(BaseApplication.sContext).setImageUrl(imageView, fullPath, iconRes);
            }
        });
    }
}
