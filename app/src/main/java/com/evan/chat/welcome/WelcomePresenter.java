package com.evan.chat.welcome;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import okhttp3.Call;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:34
 */
public class WelcomePresenter implements WelcomeContract.Presenter{

    private final WelcomeContract.View view;
    private boolean mFirstStart = true;

    public WelcomePresenter(@NonNull WelcomeContract.View view){
        this.view = checkNotNull(view,"view cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.setTitle("跳过");
        getWelcome(PropertiesUtils.getInstance().getProperty("welcome_background_url"));
    }

    private void getWelcome(String url){
        OkHttpUtils.get().url(url)
                .build().execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        view.showMessage("当前网络不可用，请检查您的网络设置");
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        view.setWelcomeIV(response);
                    }
                });
    }
}
