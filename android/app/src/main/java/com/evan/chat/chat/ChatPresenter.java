package com.evan.chat.chat;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Chat.model.Chat;

import static com.evan.chat.util.Objects.checkNotNull;
import static com.evan.chat.view.DialogView.MY;

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

    @Override
    public boolean send(String context) {
        Chat chat = new Chat();
        chat.setContent(context);
        chat.setSender(MY);
        if (view.isActive()){
            view.addDialog(chat);
            return true;
        }
        return false;
    }
}
