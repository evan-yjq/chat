package com.evan.chat.settings;

import android.support.annotation.NonNull;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/22
 * Time: 14:54
 */
public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View view;

    public SettingsPresenter(@NonNull SettingsContract.View view) {
        this.view = checkNotNull(view,"view cannot be null!");
        view.setPresenter(this);
    }


    @Override
    public void start() {

    }
}
