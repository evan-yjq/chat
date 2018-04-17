package com.evan.chat.data.source.Chat;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Chat.model.Chat;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/16
 * Time: 16:22
 */
public interface ChatDataSource {

    interface LoadAllChatCallback{

    }

    interface LoadChatCallback{

    }

    void getAllChat(@NonNull Long id, @NonNull LoadAllChatCallback callback);

    void getChat(@NonNull Long id, @NonNull LoadChatCallback callback);

    void saveChat(@NonNull Chat chat);

    void deleteChat(@NonNull Chat chat);

    void deleteAllChat();

    void refreshChats();
}
