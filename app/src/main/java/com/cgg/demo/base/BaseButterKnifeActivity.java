package com.cgg.demo.base;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import butterknife.ButterKnife;

public abstract class BaseButterKnifeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentView());
        ButterKnife.bind(this);
        initContentView();
    }

    protected abstract int provideContentView();

    protected abstract void initContentView();
}
