package com.evan.chat.data.source.Follow;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Follow.model.Follow;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:20
 */
public interface FollowDataSource {

    interface LoadAllFriendsCallback{

        void onAllFriendLoaded(List<Follow> follows);

        void onDataNotAvailable();
    }

    interface LoadFriendCallback{

        void onFriendLoaded(Follow follow);

        void onDataNotAvailable();
    }

    void getFriends(@NonNull Long id, @NonNull LoadAllFriendsCallback callback);

    void getFriend(@NonNull Long id, @NonNull LoadFriendCallback callback);

    void saveFriend(@NonNull Follow follow);

    void deleteFriend(@NonNull Long id);

    void deleteAllFriend();

    void refreshFriends();
}
