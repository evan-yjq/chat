package com.evan.chat.chat;

import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;
import com.evan.chat.data.source.model.Chat;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/13
 * Time: 21:55
 */
public interface ChatContract {

    interface View extends BaseView<Presenter> {

        void addDialog(Chat chat);
    }

    interface Presenter extends BasePresenter {

        boolean send(String context);
    }
}
