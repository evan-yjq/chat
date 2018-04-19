package com.evan.chat;

import android.app.Application;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

        try {
            PropertiesUtils.getInstance().setPath(getApplicationContext().getAssets().open("request_url.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
