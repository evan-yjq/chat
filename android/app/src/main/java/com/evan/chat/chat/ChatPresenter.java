package com.evan.chat.chat;

import android.support.annotation.NonNull;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/13
 * Time: 22:31
 */
public class ChatPresenter implements ChatContract.Presenter {

    private final ChatContract.View view;

    public ChatPresenter(@NonNull ChatContract.View view) {
        this.view = checkNotNull(view,"view cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
