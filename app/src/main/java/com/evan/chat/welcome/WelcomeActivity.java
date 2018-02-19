package com.evan.chat.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:23
 */
@Fullscreen
@EActivity(R.layout.welcome_act)
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WelcomeFragment welcomeFragment = (WelcomeFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (welcomeFragment == null){
            welcomeFragment = WelcomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),welcomeFragment,R.id.contentFrame);
        }
        new WelcomePresenter(welcomeFragment);
    }
}
