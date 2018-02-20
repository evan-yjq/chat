package com.evan.chat;

import android.app.Application;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/20
 * Time: 下午3:17
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
