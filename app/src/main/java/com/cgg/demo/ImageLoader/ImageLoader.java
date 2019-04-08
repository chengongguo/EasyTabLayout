package com.cgg.demo.ImageLoader;


import com.bumptech.glide.Glide;

import android.content.Context;
import androidx.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader implements ILoader {
    private final static String TAG = ImageLoader.class.getSimpleName();
    private Context context;

    public ImageLoader(Context context) {
        this.context = context;
    }

    @Override
    public void setImageUrl(ImageView imageView, String url, @DrawableRes int defalutIconRes) {
        Log.d(TAG, "setImageUrl() imageView=" + imageView + " url=" + url + " defalutIconRes=" + defalutIconRes);
        if (imageView == null || TextUtils.isEmpty(url)) {
            return;
        }
        if (url.endsWith("gif")) {
            Glide.with(context).load(url).asGif().into(imageView);
        } else {
            Glide.with(context).load(url).placeholder(defalutIconRes).into(imageView);
        }
    }

}
