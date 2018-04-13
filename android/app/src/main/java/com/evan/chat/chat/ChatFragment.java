package com.evan.chat.chat;

import android.support.v4.app.Fragment;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/13
 * Time: 22:30
 */
public class ChatFragment extends Fragment implements ChatContract.View {


    @Override
    public void setPresenter(ChatContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
