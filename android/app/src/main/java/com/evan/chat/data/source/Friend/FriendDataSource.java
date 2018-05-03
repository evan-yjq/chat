package com.evan.chat.data.source.Friend;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.evan.chat.data.source.User.UserDataSource;
import com.evan.chat.data.source.model.Friend;

import java.io.File;
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

    interface HeadCallback{

        void onSuccess(Bitmap bitmap);

        void onFail();
    }

    void getFriends(@NonNull LoadAllFriendsCallback callback);

    void getFriend(@NonNull Long id, @NonNull LoadFriendCallback callback);

    void saveHead(@NonNull File file, @NonNull Long id, @NonNull Bitmap bitmap);

    void getHead(@NonNull File file, @NonNull Long id, @NonNull HeadCallback callback);

    void saveFriend(@NonNull Friend friend);

    void deleteFriend(@NonNull Long id);

    void deleteAllFriend();

    void refreshFriends();
}
