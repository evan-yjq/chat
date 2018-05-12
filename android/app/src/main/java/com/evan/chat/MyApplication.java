package com.evan.chat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import com.evan.chat.util.PropertiesUtils;
import com.evan.chat.wrapper.LanguageContextWrapper;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.OkHttpClient;

import java.io.IOException;
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String l = "en";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) l = getDefaultLang();
        String lang = preferences.getString(getString(R.string.pref_locale), l);
        System.out.println(lang);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_locale),lang);
        editor.apply();
        LanguageContextWrapper.wrap(getBaseContext(),lang);

        try {
            PropertiesUtils.getInstance().setPath(getApplicationContext().getAssets().open("request_url.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private String getDefaultLang(){
        return LocaleList.getDefault().get(0).toString();
    }
}
