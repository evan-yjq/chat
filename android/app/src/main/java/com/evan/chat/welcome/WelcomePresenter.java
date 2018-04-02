package com.evan.chat.welcome;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import okhttp3.Call;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

    private AppExecutors appExecutors;

    public WelcomePresenter(@NonNull WelcomeContract.View view){
        this.view = checkNotNull(view,"view cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {
        appExecutors = new AppExecutors();
        getWelcome(PropertiesUtils.getInstance().getProperty("welcome_background_url",false));
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CountDown(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getWelcome(final String url){
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    @Override
    public Long getAutoUserId() {
        return (long)0;
    }

    static int curSec;
    private Timer timer;
    private void CountDown(int limitSec) throws InterruptedException{
        curSec = limitSec;
        timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                //可以添加每秒做的事
            }
        },0,1000);
        TimeUnit.SECONDS.sleep(limitSec);
        timeStop();
    }

    @Override
    public void timeStop(){
        timer.cancel();
        if (view.isActive()) {
            view.showLogView();
        }
    }
}
