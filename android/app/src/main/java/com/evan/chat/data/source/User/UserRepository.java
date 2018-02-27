package com.evan.chat.data.source.User;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.User.model.User;

import java.util.Map;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午7:49
 */
public class UserRepository implements UserDataSource {

    private static UserRepository INSTANCE = null;

    private final UserDataSource mUserLocalDataSource;

    private final UserDataSource mUserRemoteDataSource;

    private User user;

    private boolean mCacheIsDirty = false;

    private UserRepository(@NonNull UserDataSource userLocalDataSource,
                           @NonNull UserDataSource userRemoteDataSource){
        mUserLocalDataSource = checkNotNull(userLocalDataSource);
        mUserRemoteDataSource = checkNotNull(userRemoteDataSource);
    }

    public static UserRepository getInstance(@NonNull UserDataSource userRemoteDataSource,
                                             @NonNull UserDataSource userLocalDataSource){
        if (INSTANCE == null){
            INSTANCE = new UserRepository(userLocalDataSource,userRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NonNull LoadAllUserCallback callback) {

    }

    @Override
    public void getUser(@NonNull Long id, LoadUserCallback callback) {

    }

    @Override
    public void saveUser(@NonNull User user) {

    }

    @Override
    public void deleteUser(@NonNull Long id) {

    }

    @Override
    public void deleteAllUser() {

    }

    @Override
    public void check(@NonNull final String account, @NonNull final String password, @NonNull final CheckCallback callback) {
        checkNotNull(account);
        checkNotNull(password);
        checkNotNull(callback);
        if (true) {
            mUserRemoteDataSource.check(account, password, callback);
        }else {
            mUserLocalDataSource.check(account, password, callback);
        }
    }

    @Override
    public void refreshUsers() {

    }
}
