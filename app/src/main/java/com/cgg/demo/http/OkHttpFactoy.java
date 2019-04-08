package com.cgg.demo.http;


import com.cgg.demo.base.BaseApplication;
import com.cgg.demo.utils.NetWorkUtil;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpFactoy {
    private final static String TAG = OkHttpFactoy.class.getSimpleName();
    private static OkHttpClient mOkHttpClient;

    static OkHttpClient getOkHttpClient(Interceptor... interceptors) {

        if (mOkHttpClient == null) {
            synchronized (OkHttpFactoy.class) {
                if (mOkHttpClient == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    File cacheFile = new File(BaseApplication.sContext.getCacheDir(), "cache");
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

                    OkHttpClient.Builder build = new OkHttpClient.Builder();
                    if (interceptors != null && interceptors.length > 0) {
                        for (Interceptor interceptor : interceptors) {
                            build.addInterceptor(interceptor);
                        }
                    }
                    mOkHttpClient = build
                            .addInterceptor(sHeaderInterceptor)
                            .addInterceptor(loggingInterceptor)
                            //                        .addInterceptor(sLogInterceptor)
                            .addInterceptor(sCacheInterceptor)
                            //                        .addInterceptor(sHttpCacheInterceptor)
                            //                       .addNetworkInterceptor(sHttpCacheInterceptor)
                            .cache(cache)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 增加头部信息的拦截器
     */
    private static Interceptor sHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
            builder.addHeader("Cache-Control", "max-age=0");
            builder.addHeader("Upgrade-Insecure-Requests", "1");
            builder.addHeader("X-Requested-With", "XMLHttpRequest");
            builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
            return chain.proceed(builder.build());
        }
    };

    private static Interceptor sHttpCacheInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetworkAvailable(BaseApplication.sContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.d("Okhttp", "no network");
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetworkAvailable(BaseApplication.sContext)) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    //缓存配置
    private static Interceptor sCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if (!NetWorkUtil.isNetworkAvailable(BaseApplication.sContext)) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetworkAvailable(BaseApplication.sContext)) {
                int maxAge = 0; // read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

}
