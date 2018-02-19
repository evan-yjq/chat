package com.evan.chat.welcome;

import android.support.annotation.NonNull;

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
        view.setTitle("测试");
    }
}
