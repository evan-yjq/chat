package com.evan.chat.data.source.Chat;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Chat.model.Chat;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/17
 * Time: 14:52
 */
public class ChatRepository implements ChatDataSource {

    private static ChatRepository INSTANCE = null;

    private final ChatDataSource local;
    private final ChatDataSource remote;

    private Map<Long, List<Chat>>chats;

    private boolean mCacheIsDirty = false;

    private ChatRepository(@NonNull ChatDataSource local, @NonNull ChatDataSource remote) {
        this.local = local;
        this.remote = remote;
    }

    public static ChatRepository getInstance(@NonNull ChatDataSource local, @NonNull ChatDataSource remote){
        if (INSTANCE == null){
            INSTANCE = new ChatRepository(local, remote);
        }
        return INSTANCE;
    }

    @Override
    public void getAllChat(@NonNull Long id, @NonNull LoadAllChatCallback callback) {

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
