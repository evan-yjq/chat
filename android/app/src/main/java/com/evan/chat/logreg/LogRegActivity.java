package com.evan.chat.logreg;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class LogRegActivity extends AppCompatActivity {
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

        int userId = getIntent().getIntExtra(EXTRA_USER_ID, 0);
        RegFragment regFragment = null;
        LogFragment logFragment = null;
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (f != null && "RegFragment".equals(f.getClass().getSimpleName())) {
            regFragment = (RegFragment) f;
        } else {
            logFragment = (LogFragment) f;
        }

        if (logFragment == null) {
            logFragment = LogFragment.newInstance();
        }
        if (regFragment == null) {
            regFragment = RegFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), regFragment, R.id.contentFrame);
        }

        logRegPresenter = new LogRegPresenter(
                logFragment,
                regFragment,
                Injection.provideSignInUser(getApplicationContext()),
                Injection.provideRegUser(getApplicationContext()),
                Injection.provideUseCaseHandler(),
                userId);
    }
}
