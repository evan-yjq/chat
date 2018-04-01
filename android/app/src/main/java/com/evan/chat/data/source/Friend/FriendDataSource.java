package com.evan.chat.data.source.Friend;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Friend.model.Friend;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:20
 */
public interface FriendDataSource {

    interface LoadAllFriendsCallback{

        void onAllFriendLoaded(List<Friend> friends);

        void onDataNotAvailable();
    }

    interface LoadFriendCallback{

        void onFriendLoaded(Friend friend);

        void onDataNotAvailable();
    }

    void getFriends(@NonNull Long id, @NonNull LoadAllFriendsCallback callback);

    void getFriend(@NonNull Long id, @NonNull LoadFriendCallback callback);

    void saveFriend(@NonNull Friend friend);

    void deleteFriend(@NonNull Long id);

    void deleteAllFriend();

    void refreshFriends();
}
