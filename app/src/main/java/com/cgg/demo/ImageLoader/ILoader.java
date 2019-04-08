package com.cgg.demo.ImageLoader;

import androidx.annotation.DrawableRes;
import android.widget.ImageView;

public interface ILoader {
    void setImageUrl(ImageView imageView, String url, @DrawableRes int defalutIconRes);
}
