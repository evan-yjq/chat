package com.evan.chat.logreg;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;
import org.androidannotations.annotations.EActivity;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:28
 */
@EActivity(R.layout.log_reg_act)
public class LogRegActivity extends AppCompatActivity{
    //当前视图
    private static final String LOG_REG_KEY = "LOG_REG_KEY";

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private LogRegPresenter logRegPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color6));
        }

        int userId = getIntent().getIntExtra(EXTRA_USER_ID,0);

        LogFragment logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        RegFragment regFragment = (RegFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (logFragment == null){
            logFragment = LogFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),logFragment,R.id.contentFrame);
        }
        if (regFragment == null){
            regFragment = RegFragment.newInstance();
        }

        logRegPresenter = new LogRegPresenter(
                logFragment,
                regFragment,
                Injection.provideSignInUser(getApplicationContext()),
                Injection.provideUseCaseHandler(),
                userId);
    }
}
