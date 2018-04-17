package com.evan.chat.data.source.Chat;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Chat.model.Chat;
import com.evan.chat.data.source.dao.ChatDao;
import com.evan.chat.util.AppExecutors;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/17
 * Time: 14:51
 */
public class ChatLocalDataSource implements ChatDataSource {

    private static volatile ChatLocalDataSource INSTANCE;

    private ChatDao mChatDao;

    private AppExecutors mAppExecutors;

    private ChatLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull ChatDao chatDao){
        mAppExecutors = appExecutors;
        mChatDao = chatDao;
    }

    public static ChatLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull ChatDao chatDao){
        if (INSTANCE == null){
            synchronized (ChatLocalDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new ChatLocalDataSource(appExecutors, chatDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllChat(@NonNull Long id, @NonNull LoadAllChatCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void getChat(@NonNull Long id, @NonNull LoadChatCallback callback) {

    }

    @Override
    public void saveChat(@NonNull Chat chat) {

    }

    @Override
    public void deleteChat(@NonNull Chat chat) {

    }

    @Override
    public void deleteAllChat() {

    }

    @Override
    public void refreshChats() {

    }
}
