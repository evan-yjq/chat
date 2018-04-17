package com.evan.chat.data.source.Chat;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Chat.model.Chat;
import com.evan.chat.util.AppExecutors;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/17
 * Time: 14:51
 */
public class ChatRemoteDataSource implements ChatDataSource {

    private static ChatRemoteDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    private ChatRemoteDataSource(@NonNull AppExecutors appExecutors){
        mAppExecutors = appExecutors;
    }

    public static ChatRemoteDataSource getInstance(@NonNull AppExecutors appExecutors){
        if (INSTANCE == null){
            synchronized (ChatRemoteDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new ChatRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllChat(@NonNull Long id, @NonNull LoadAllChatCallback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
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
