package com.evan.chat.welcome;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.model.User;
import com.evan.chat.domain.usecase.SignInUser;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.evan.chat.domain.usecase.GetAutoUser;
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
    private final GetAutoUser getAutoUser;
    private final SignInUser signInUser;
    private final UseCaseHandler useCaseHandler;

    private boolean mAutoSignLock = true;
    private User autoUser;

    private AppExecutors appExecutors;

    WelcomePresenter(@NonNull WelcomeContract.View view, @NonNull GetAutoUser getAutoUser,
                     @NonNull UseCaseHandler useCaseHandler, @NonNull SignInUser signInUser){
        this.view = checkNotNull(view,"view cannot be null!");
        this.getAutoUser = checkNotNull(getAutoUser,"getAutoUser cannot be null!");
        this.useCaseHandler = checkNotNull(useCaseHandler,"useCaseHandler cannot be null!");
        this.signInUser = checkNotNull(signInUser,"signInUser cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {
        appExecutors = new AppExecutors();
        getWelcome(PropertiesUtils.getInstance().getProperty("welcome_background_url",false));
        setAutoUser();
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CountDown3();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取欢迎界面图片
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

    private void setAutoUser(){
        useCaseHandler.execute(getAutoUser, new GetAutoUser.RequestValues(),
                new UseCase.UseCaseCallback<GetAutoUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetAutoUser.ResponseValue response) {
                        autoUser = response.getUser();
                        useCaseHandler.execute(signInUser, new SignInUser.RequestValues(autoUser.getAccount(), autoUser.getPassword()),
                                new UseCase.UseCaseCallback<SignInUser.ResponseValue>() {
                                    @Override
                                    public void onSuccess(SignInUser.ResponseValue response) {
                                        autoUser = response.getUser();
                                        mAutoSignLock = false;
                                    }

                                    @Override
                                    public void onError() {
                                        autoUser = null;
                                        mAutoSignLock = false;
                                    }
                                });
                    }

                    @Override
                    public void onError() {
                        autoUser = null;
                        mAutoSignLock = false;
                    }
                });
    }

    @Override
    public User getAutoUser() {
        return autoUser;
    }

    private Timer timer;
    private void CountDown3() throws InterruptedException{
        timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                //可以添加每秒做的事
            }
        },0,1000);
        TimeUnit.SECONDS.sleep(3);
        timeStop();
    }

    @Override
    public void timeStop() throws InterruptedException {
        while(mAutoSignLock)
            Thread.sleep(300);
        timer.cancel();
        if (view.isActive())
            view.showNextView();
    }
}
