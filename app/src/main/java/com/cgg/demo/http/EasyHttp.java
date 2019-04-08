package com.cgg.demo.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EasyHttp {
    private static final String TAG = EasyHttp.class.getSimpleName();
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static ExecutorService sExecutorService = Executors.newSingleThreadExecutor();

    public interface GetDataListener {
        void onSuccess(String data);
    }

    public interface RequestListener extends GetDataListener {
        void onFail(String msg);
    }

    public static void get(String url, GetDataListener listener) {
        request(url, false, null, listener);
    }

    public static void post(String url, String requestBody, GetDataListener listener) {
        request(url, true, requestBody, listener);
    }

    public static void request(String url, boolean isPost, String requestBody, final GetDataListener listener) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null && listener instanceof RequestListener) {
                            ((RequestListener) listener).onFail(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            try {
                                listener.onSuccess(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        Request.Builder builder = new Request.Builder().url(url);
        if (isPost) {
            MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
            builder.post(RequestBody.create(mediaType, requestBody));
        }
        Request request = builder.build();
        OkHttpFactoy.getOkHttpClient().newCall(request).enqueue(callback);
    }

    public interface ExecuteListener {
        void onSuccess(Map<String, String> dataMap);
    }

    public static void get(final Map<String, String> urlMap, final ExecuteListener listener) {
        sExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> dataMap = new HashMap<>();
                    for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                        String key = entry.getKey();
                        String url = entry.getValue();
                        Request request = new Request.Builder().url(url).build();
                        Response response = OkHttpFactoy.getOkHttpClient().newCall(request).execute();
                        if (response == null) {
                            Log.i(TAG, "get() response is null, url:" + url);
                            continue;
                        }
                        if (!response.isSuccessful()) {
                            Log.i(TAG, "get() response error, code:" + response.code() + " msg:" + response.message() + " url:" + url);
                            continue;
                        }
                        if (response.body() == null) {
                            Log.i(TAG, "get() response body is null, url:" + url);
                            continue;
                        }
                        String data = response.body().string();
                        dataMap.put(key, data);
                    }
                    Log.i(TAG, "get() dataMap:" + dataMap.toString());
                    if (listener != null) {
                        listener.onSuccess(dataMap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
