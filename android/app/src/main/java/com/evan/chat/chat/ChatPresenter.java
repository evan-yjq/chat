package com.evan.chat.chat;

import android.support.annotation.NonNull;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.domain.usecase.Chat.SendMessage;
import com.evan.chat.data.source.model.Chat;
import com.evan.chat.data.source.model.Friend;

import java.util.Date;

import static com.evan.chat.util.Objects.checkNotNull;
import static com.evan.chat.view.DialogView.MY;
import static com.evan.chat.PublicData.user;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/13
 * Time: 22:31
 */
public class ChatPresenter implements ChatContract.Presenter {

    private final ChatContract.View view;
    private final Long userId;
    private final Long friendId;
    private final SendMessage sendMessage;
    private final UseCaseHandler mUseCaseHandler;

    public ChatPresenter(@NonNull ChatContract.View view, @NonNull Friend friend,
                         @NonNull SendMessage sendMessage, @NonNull UseCaseHandler useCaseHandler) {
        this.view = checkNotNull(view,"view cannot be null!");
        checkNotNull(friend,"friend cannot be null!");
        this.sendMessage = checkNotNull(sendMessage,"sendMessage cannot be null!");
        mUseCaseHandler = checkNotNull(useCaseHandler,"useCaseHandler cannot be null!");
        this.userId = user.getId();
        this.friendId = friend.getId();
        view.setPresenter(this);
    }

    @Override
    public void start() {
        //todo 加载聊天记录
    }

    @Override
    public boolean send(String context) {
        Chat chat = new Chat(null, new Date().getTime(), userId, friendId, MY, context);

//        mUseCaseHandler.execute(sendMessage, new SendMessage.RequestValues(chat),
//                new UseCase.UseCaseCallback<SendMessage.ResponseValue>() {
//                    @Override
//                    public void onSuccess(SendMessage.ResponseValue response) {
//
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });
        if (view.isActive()){
            view.addDialog(chat);
            return true;
        }

        return false;
    }
}
