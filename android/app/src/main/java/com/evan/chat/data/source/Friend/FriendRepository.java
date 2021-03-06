package com.evan.chat.data.source.Friend;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.evan.chat.data.source.model.Friend;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Long, Friend> mCachedFriends;
    private Map<Long, Bitmap> mHeads;

    private boolean mCacheIsDirty = false;

    private FriendRepository(@NonNull FriendDataSource mFriendLocalDataSource,
                             @NonNull FriendDataSource mFriendRemoteDataSource){
        this.mFriendLocalDataSource = mFriendLocalDataSource;
        this.mFriendRemoteDataSource = mFriendRemoteDataSource;
    }

    public static FriendRepository getInstance(@NonNull FriendDataSource mFriendRemoteDataSource,
                                               @NonNull FriendDataSource mFriendLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FriendRepository(mFriendLocalDataSource, mFriendRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getFriends(@NonNull final LoadAllFriendsCallback callback) {
        checkNotNull(callback);

        if (mCachedFriends != null && !mCacheIsDirty){
            callback.onAllFriendLoaded(new ArrayList<>(mCachedFriends.values()));
            return;
        }
        if (mCacheIsDirty) {
            getFriendsFromRemoteDataSource(callback);
        }else{
            mFriendLocalDataSource.getFriends(new LoadAllFriendsCallback() {
                @Override
                public void onAllFriendLoaded(List<Friend> friends) {
                    refreshCache(friends);
                    callback.onAllFriendLoaded(new ArrayList<>(friends));
                }

                @Override
                public void onDataNotAvailable() {
                    getFriendsFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getFriendsFromRemoteDataSource(@NonNull final LoadAllFriendsCallback callback){
        mFriendRemoteDataSource.getFriends(new LoadAllFriendsCallback() {
            @Override
            public void onAllFriendLoaded(List<Friend> friends) {
                refreshCache(friends);
                refreshLocalDataSource(friends);
                callback.onAllFriendLoaded(new ArrayList<>(friends));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Friend> friends) {
        if (mCachedFriends == null) {
            mCachedFriends = new LinkedHashMap<>();
        }
        mCachedFriends.clear();
        for (Friend friend : friends) {
            mCachedFriends.put(friend.getId(), friend);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void getFriend(@NonNull final Long id, @NonNull final LoadFriendCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);

        final Friend cacheFriend = getFriendWithId(id);

        if (cacheFriend != null && !mCacheIsDirty){
            callback.onFriendLoaded(cacheFriend);
            return;
        }

        if (mCacheIsDirty){
            getFriendFromRemoteDataSource(id, callback);
        }else {
            mFriendLocalDataSource.getFriend(id, new LoadFriendCallback() {
                @Override
                public void onFriendLoaded(Friend friend) {
                    if (mCachedFriends == null){
                        mCachedFriends = new LinkedHashMap<>();
                    }
                    mCachedFriends.put(friend.getId(), friend);
                    callback.onFriendLoaded(friend);
                }

                @Override
                public void onDataNotAvailable() {
                    getFriendFromRemoteDataSource(id, callback);
                }
            });
        }
    }

    @Override
    public void saveHead(@NonNull File file, @NonNull Long id, @NonNull Bitmap bitmap) {
        checkNotNull(file);
        checkNotNull(id);
        checkNotNull(bitmap);
        mFriendLocalDataSource.saveHead(file, id, bitmap);
    }

    @Override
    public void getHead(@NonNull final File file, @NonNull final Long id, @NonNull final HeadCallback callback) {
        checkNotNull(file);
        checkNotNull(id);
        checkNotNull(callback);
        Bitmap head = getHeadWithId(id);
        if (head != null){
            callback.onSuccess(head);
            return;
        }
        mFriendLocalDataSource.getHead(file, id, new HeadCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                callback.onSuccess(bitmap);
            }

            @Override
            public void onFail() {
                mFriendRemoteDataSource.getHead(file, id, new HeadCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        mFriendLocalDataSource.saveHead(file,id,bitmap);
                        if (mHeads == null){
                            mHeads = new LinkedHashMap<>();
                        }
                        mHeads.put(id, bitmap);
                        callback.onSuccess(bitmap);
                    }

                    @Override
                    public void onFail() {
                        callback.onFail();
                    }
                });
            }
        });
    }

    @Nullable
    private Bitmap getHeadWithId(@NonNull Long id){
        checkNotNull(id);
        if (mHeads == null || mHeads.isEmpty()){
            return null;
        }else{
            return mHeads.get(id);
        }
    }

    private void getFriendFromRemoteDataSource(@NonNull Long id, @NonNull final LoadFriendCallback callback){
        mFriendRemoteDataSource.getFriend(id, new LoadFriendCallback() {
            @Override
            public void onFriendLoaded(Friend friend) {
                if (mCachedFriends == null){
                    mCachedFriends = new LinkedHashMap<>();
                }
                mCachedFriends.put(friend.getId(), friend);
                callback.onFriendLoaded(friend);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Nullable
    private Friend getFriendWithId(@NonNull Long id){
        checkNotNull(id);
        if (mCachedFriends == null || mCachedFriends.isEmpty()){
            return null;
        }else{
            return mCachedFriends.get(id);
        }
    }

    @Override
    public void saveFriend(@NonNull Friend friend) {
        checkNotNull(friend);

        mFriendRemoteDataSource.saveFriend(friend);
        mFriendLocalDataSource.saveFriend(friend);

        if (mCachedFriends == null){
            mCachedFriends = new LinkedHashMap<>();
        }
        mCachedFriends.put(friend.getId(), friend);
    }

    @Override
    public void deleteFriend(@NonNull Long id) {

    }

    @Override
    public void deleteAllFriend() {

    }

    @Override
    public void refreshFriends() {
        mCacheIsDirty = true;
    }

    private void refreshLocalDataSource(List<Friend> friends) {
        mFriendLocalDataSource.deleteAllFriend();
        for (Friend friend : friends) {
            mFriendLocalDataSource.saveFriend(friend);
        }
    }
}
