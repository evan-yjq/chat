package com.evan.chat.welcome;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:23
 */
@SuppressLint("Registered")
@Fullscreen
@EActivity(R.layout.welcome_act)
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        WelcomeFragment welcomeFragment = (WelcomeFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (welcomeFragment == null){
            welcomeFragment = WelcomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),welcomeFragment,R.id.contentFrame);
        }
        new WelcomePresenter(welcomeFragment,
                Injection.provideGetAutoUser(getApplicationContext()),
                Injection.provideUseCaseHandler(),
                Injection.provideSignInUser(getApplicationContext()));
        start();
    }

    @UiThread
    public void start(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
