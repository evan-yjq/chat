package com.evan.chat.data.source.Follow;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Follow.model.Follow;

import java.util.List;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:37
 */
public class FollowRepository implements FollowDataSource {

    private static FollowRepository INSTANCE = null;

    private final FollowDataSource mFriendLocalDataSource;

    private final FollowDataSource mFriendRemoteDataSource;

    private List<Follow> follows;

    private boolean mCacheIsDirty = false;

    private FollowRepository(@NonNull FollowDataSource mFriendLocalDataSource,
                             @NonNull FollowDataSource mFriendRemoteDataSource){
        this.mFriendLocalDataSource = mFriendLocalDataSource;
        this.mFriendRemoteDataSource = mFriendRemoteDataSource;
    }

    public static FollowRepository getInstance(@NonNull FollowDataSource mFriendRemoteDataSource,
                                               @NonNull FollowDataSource mFriendLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FollowRepository(mFriendLocalDataSource, mFriendRemoteDataSource);
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
    public void saveFriend(@NonNull Follow follow) {

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
