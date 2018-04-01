package com.evan.chat.data.source.Friend;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Friend.model.Friend;

import java.util.List;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:37
 */
public class FriendRepository implements FriendDataSource {

    private static FriendRepository INSTANCE = null;

    private final FriendDataSource mFriendLocalDataSource;

    private final FriendDataSource mFriendRemoteDataSource;

    private List<Friend>friends;

    private boolean mCacheIsDirty = false;

    private FriendRepository(@NonNull FriendDataSource mFriendLocalDataSource,
                             @NonNull FriendDataSource mFriendRemoteDataSource){
        this.mFriendLocalDataSource = mFriendLocalDataSource;
        this.mFriendRemoteDataSource = mFriendRemoteDataSource;
    }

    public static FriendRepository getInstance(@NonNull FriendDataSource mFriendLocalDataSource,
                                               @NonNull FriendDataSource mFriendRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FriendRepository(mFriendLocalDataSource, mFriendRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getFriends(@NonNull Long id, @NonNull LoadAllFriendsCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);
        mFriendRemoteDataSource.getFriends(id,callback);
    }

    @Override
    public void getFriend(@NonNull Long id, @NonNull LoadFriendCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);
        mFriendLocalDataSource.getFriend(id,callback);
    }

    @Override
    public void saveFriend(@NonNull Friend friend) {

    }

    @Override
    public void deleteFriend(@NonNull Long id) {

    }

    @Override
    public void deleteAllFriend() {

    }

    @Override
    public void refreshFriends() {

    }
}
