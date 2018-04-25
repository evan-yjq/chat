package com.evan.chat.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.evan.chat.PublicData;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.model.Setting;
import com.evan.chat.data.source.model.User;
import com.evan.chat.domain.usecase.GetSetting;
import com.evan.chat.domain.usecase.SignInUser;
import com.evan.chat.face.FaceActivity;
import com.evan.chat.face.FaceActivity_;
import com.evan.chat.face.FaceFragment;
import com.evan.chat.friends.FriendsActivity;
import com.evan.chat.logreg.LogRegActivity;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.evan.chat.domain.usecase.GetAutoUser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import okhttp3.Call;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.evan.chat.settings.domain.SettingKey.AUTO_LOGIN_OPEN_ID;
import static com.evan.chat.settings.domain.SettingKey.FACE_LOGIN_OPEN_ID;
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
    private final GetSetting getSetting;
    private final SignInUser signInUser;
    private final UseCaseHandler useCaseHandler;
    private final Context context;

    private boolean mAutoSignLock = true;
    private User autoUser;

    private AppExecutors appExecutors;

    WelcomePresenter(@NonNull WelcomeContract.View view, @NonNull GetAutoUser getAutoUser,
                     @NonNull UseCaseHandler useCaseHandler, @NonNull SignInUser signInUser,
                     @NonNull GetSetting getSetting, @NonNull Context context){
        this.view = checkNotNull(view,"view cannot be null!");
        this.getAutoUser = checkNotNull(getAutoUser,"getAutoUser cannot be null!");
        this.getSetting = checkNotNull(getSetting,"getSetting cannot be null!");
        this.useCaseHandler = checkNotNull(useCaseHandler,"useCaseHandler cannot be null!");
        this.signInUser = checkNotNull(signInUser,"signInUser cannot be null!");
        this.context = checkNotNull(context,"context cannot be null!");
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

    private void isAutoLogin(){
        useCaseHandler.execute(getSetting, new GetSetting.RequestValues(AUTO_LOGIN_OPEN_ID),
                new UseCase.UseCaseCallback<GetSetting.ResponseValue>() {
                    @Override
                    public void onSuccess(GetSetting.ResponseValue response) {
                        if (Boolean.parseBoolean(response.getSetting().getValue())) {
                            signInUser();
                        }else {
                            autoUser = null;
                            mAutoSignLock = false;
                        }
                    }

                    @Override
                    public void onError() {
                        autoUser = null;
                        mAutoSignLock = false;
                    }
                });
    }

    private void signInUser(){
        useCaseHandler.execute(signInUser, new SignInUser.RequestValues(autoUser.getAccount(), autoUser.getPassword()),
                new UseCase.UseCaseCallback<SignInUser.ResponseValue>() {
                    @Override
                    public void onSuccess(SignInUser.ResponseValue response) {
                        autoUser = response.getUser();
                        PublicData.user = autoUser;
                        mAutoSignLock = false;
                    }

                    @Override
                    public void onError() {
                        autoUser = null;
                        mAutoSignLock = false;
                    }
                });
    }

    private void setAutoUser(){
        useCaseHandler.execute(getAutoUser, new GetAutoUser.RequestValues(),
                new UseCase.UseCaseCallback<GetAutoUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetAutoUser.ResponseValue response) {
                        autoUser = response.getUser();
                        isAutoLogin();
                    }

                    @Override
                    public void onError() {
                        autoUser = null;
                        mAutoSignLock = false;
                    }
                });
    }

    private void isFaceLogin(){
        useCaseHandler.execute(getSetting, new GetSetting.RequestValues(FACE_LOGIN_OPEN_ID),
                new UseCase.UseCaseCallback<GetSetting.ResponseValue>() {
                    @Override
                    public void onSuccess(GetSetting.ResponseValue response) {
                        if (view.isActive()){
                            if (Boolean.parseBoolean(response.getSetting().getValue())){
                                Intent intent = new Intent(context, FaceActivity_.class);
                                intent.putExtra(FaceActivity.EXTRA_FACE_VIEW,FaceFragment.DIST);
                                intent.putExtra(FaceActivity.EXTRA_IS_RESULT, true);
                                ((Fragment)view).startActivityForResult(intent, FaceActivity.REQUEST_FACE_DISK);
                            }else{
                                view.showNextView(new Intent(context, FriendsActivity.class));
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        if (view.isActive())
                            view.showNextView(new Intent(context, FriendsActivity.class));
                    }
                });
    }

    private volatile boolean timerStop = false;
    private Timer timer;
    private void CountDown3() throws InterruptedException{
        timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run(){
                //可以添加每秒做的事
            }
        },0,1000);
        TimeUnit.SECONDS.sleep(3);
        if (!timerStop) timeStop();
    }

    @Override
    public void timeStop() throws InterruptedException {
        timerStop = true;
        while(mAutoSignLock)
            Thread.sleep(300);
        timer.cancel();
        getNextView();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (FaceActivity.REQUEST_FACE_DISK == requestCode)
            if (view.isActive())
                if (Activity.RESULT_OK == resultCode)
                    view.showNextView(new Intent(context, FriendsActivity.class));
                else view.showNextView(new Intent(context, LogRegActivity.class));
    }

    @Override
    public void getNextView(){
        Intent intent;
        if (autoUser==null) {
            if (view.isActive()) {
                view.showNextView(new Intent(context, LogRegActivity.class));
            }
        }else {
            isFaceLogin();
        }
    }
}
