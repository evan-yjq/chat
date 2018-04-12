package com.evan.chat.follows;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;
import org.androidannotations.annotations.EActivity;

import static com.evan.chat.logreg.LogRegActivity.EXTRA_USER_ID;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:50
 */
@EActivity(R.layout.follows_act)
public class FollowsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Long userId = getIntent().getLongExtra(EXTRA_USER_ID,0);
        FollowsFragment followsFragment = (FollowsFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (followsFragment == null){
            followsFragment = FollowsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), followsFragment,R.id.contentFrame);
        }

        new FollowsPresenter(followsFragment,
                Injection.provideUseCaseHandler(),
                Injection.provideGetFriends(getApplicationContext()),
                userId);

    }
}
