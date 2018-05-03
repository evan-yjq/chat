package com.evan.chat.data.source.Friend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.data.source.dao.FriendDao;
import com.evan.chat.util.AppExecutors;

import java.io.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:36
 */
public class FriendLocalDataSource implements FriendDataSource {

    private static volatile FriendLocalDataSource INSTANCE;

    private FriendDao mFriendDao;

    private AppExecutors mAppExecutors;

    private FriendLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull FriendDao friendDao){
        mAppExecutors = appExecutors;
        mFriendDao = friendDao;
    }

    public static FriendLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull FriendDao friendDao){
        if (INSTANCE == null){
            synchronized (FriendLocalDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new FriendLocalDataSource(appExecutors,friendDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getFriends(@NonNull final LoadAllFriendsCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Friend>friends = mFriendDao.loadAll();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (friends.isEmpty()){
                            callback.onDataNotAvailable();
                        }else{
                            callback.onAllFriendLoaded(friends);
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
                final Friend friend = mFriendDao.load(id);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (friend == null){
                            callback.onDataNotAvailable();
                        }else{
                            callback.onFriendLoaded(friend);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void saveHead(@NonNull final File file, @NonNull final Long id, @NonNull final Bitmap bitmap) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File picFile = new File(file, "/head/"+id+".png");
                    FileOutputStream fos = new FileOutputStream(picFile);
                    fos.write(Bitmap2Bytes(bitmap));
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Bitmap转byte数组
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);//png类型
        return baos.toByteArray();
    }

    @Override
    public void getHead(@NonNull final File file, @NonNull final Long id, @NonNull final HeadCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File picFile = new File(file, "/head/"+id+".png");
                    FileInputStream fis = new FileInputStream(picFile);
                    Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                    callback.onSuccess(bitmap);
                } catch (FileNotFoundException e) {
                    callback.onFail();
                }
            }
        });
    }

    @Override
    public void saveFriend(@NonNull final Friend friend) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFriendDao.save(friend);
            }
        });
    }

    @Override
    public void deleteFriend(@NonNull final Long id) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFriendDao.deleteByKey(id);
            }
        });
    }

    @Override
    public void deleteAllFriend() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFriendDao.deleteAll();
            }
        });
    }

    @Override
    public void refreshFriends() {

    }
}
