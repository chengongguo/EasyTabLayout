package com.cgg.demo.main;

import com.cgg.demo.R;
import com.cgg.demo.base.BaseFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import butterknife.BindView;

public class DefaultFragment extends BaseFragment {

    @BindView(R.id.fragment_name)
    TextView mFragmentName;
    @BindView(R.id.fragment_title)
    TextView mFragmentTitle;

    @Override
    protected int provideContentView() {
        return R.layout.fragment_default;
    }

    protected void initContentView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("tabName");
            String title = bundle.getString("tabTitle");
            if (!TextUtils.isEmpty(name)) {
                mFragmentName.setText(name);
            }
            if (!TextUtils.isEmpty(title)) {
                mFragmentTitle.setText(title);
            }
        }
    }
}
