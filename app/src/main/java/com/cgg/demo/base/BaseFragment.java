package com.cgg.demo.base;

import com.cgg.demo.config.Config;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = Config.TAG_PREFIX + BaseFragment.class.getSimpleName();
    protected Context mContext;
    protected View mRootView;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            int resource = provideContentView();
            if (resource != 0) {
                mRootView = inflater.inflate(resource, container, false);
                ButterKnife.bind(this, mRootView);
                initContentView();
            }
        }
        return mRootView;
    }

    protected abstract int provideContentView();


    protected void initContentView() {
    }

    protected <T> T getArguments(String param, T defaultValue) {
        Bundle bundle = getArguments();
        T data = null;
        if (bundle == null) {
            return null;
        }
        try {
            data = (T) bundle.get(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null) {
            return defaultValue;
        }
        return data;
    }
}
