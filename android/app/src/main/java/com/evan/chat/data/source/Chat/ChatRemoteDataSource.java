package com.evan.chat.data.source.Chat;

import android.support.annotation.NonNull;
import com.evan.chat.chat.domain.model.ClientSocket;
import com.evan.chat.data.source.Chat.model.Chat;
import com.evan.chat.util.AppExecutors;

import java.io.IOException;
import java.net.Socket;
import com.evan.chat.util.PropertiesUtils;
import com.google.gson.Gson;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/17
 * Time: 14:51
 */
public class ChatRemoteDataSource implements ChatDataSource {

    private static ChatRemoteDataSource INSTANCE;
    private Socket socket;
    private AppExecutors mAppExecutors;

    private ChatRemoteDataSource(@NonNull AppExecutors appExecutors){
        mAppExecutors = appExecutors;
        conn();
    }

    private void conn(){
        if (socket == null){
            mAppExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(PropertiesUtils.getInstance().getIP(), 8888);
                        socket.setKeepAlive(true);
                        socket.setSoTimeout(10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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
    public void saveChat(@NonNull final Chat chat) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                ClientSocket s = new ClientSocket(socket);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(chat, Chat.class);
                s.send(jsonStr);
            }
        });
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
