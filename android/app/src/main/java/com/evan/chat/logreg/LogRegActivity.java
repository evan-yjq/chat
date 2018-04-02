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

    static final String LOG_FRAG = "LOG_FRAG";
    static final String REG_FRAG = "REG_FRAG";

    static Fragment LOG_REG_SWITCH;

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color6));
        }

        Long userId = getIntent().getLongExtra(EXTRA_USER_ID, 0);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        RegFragment regFragment;
        LogFragment logFragment;
        if (fragment == null) {
            regFragment = RegFragment.newInstance();
            logFragment = LogFragment.newInstance();
            fragment = logFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
            LOG_REG_SWITCH = regFragment;
        }else{
            if ("RegFragment".equals(fragment.getClass().getSimpleName())) {
                regFragment = (RegFragment) fragment;
                logFragment = (LogFragment) LOG_REG_SWITCH;
            } else {
                logFragment = (LogFragment) fragment;
                regFragment = (RegFragment) LOG_REG_SWITCH;
            }
        }

        new LogRegPresenter(
                logFragment,
                regFragment,
                Injection.provideSignInUser(getApplicationContext()),
                Injection.provideRegUser(getApplicationContext()),
                Injection.provideUseCaseHandler(),
                userId);
    }
}
