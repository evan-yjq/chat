package com.evan.chat.data.source.Follow;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Follow.model.Follow;
import com.evan.chat.data.source.dao.FollowDao;
import com.evan.chat.util.AppExecutors;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:36
 */
public class FollowLocalDataSource implements FollowDataSource {

    private static volatile FollowLocalDataSource INSTANCE;

    private FollowDao mFollowDao;

    private AppExecutors mAppExecutors;

    private FollowLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull FollowDao followDao){
        mAppExecutors = appExecutors;
        mFollowDao = followDao;
    }

    public static FollowLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull FollowDao followDao){
        if (INSTANCE == null){
            synchronized (FollowLocalDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new FollowLocalDataSource(appExecutors, followDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getFriends(@NonNull Long id, @NonNull final LoadAllFriendsCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Follow> follows = mFollowDao.loadAll();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (follows.isEmpty()){
                            callback.onDataNotAvailable();
                        }else{
                            callback.onAllFriendLoaded(follows);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getFriend(@NonNull final Long id, @NonNull final LoadFriendCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Follow follow = mFollowDao.load(id);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (follow == null){
                            callback.onDataNotAvailable();
                        }else{
                            callback.onFriendLoaded(follow);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void saveFriend(@NonNull final Follow follow) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFollowDao.save(follow);
            }
        });
    }

    @Override
    public void deleteFriend(@NonNull final Long id) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFollowDao.deleteByKey(id);
            }
        });
    }

    @Override
    public void deleteAllFriend() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFollowDao.deleteAll();
            }
        });
    }

    @Override
    public void refreshFriends() {

    }
}
