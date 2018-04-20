package com.evan.chat.data.source.User;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.User.model.User;

import java.util.ArrayList;
import java.util.List;
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

    private final UserDataSource local;

    private final UserDataSource remote;

    private User user;

    private boolean mCacheIsDirty = false;

    private UserRepository(@NonNull UserDataSource userLocalDataSource,
                           @NonNull UserDataSource userRemoteDataSource) {
        local = checkNotNull(userLocalDataSource);
        remote = checkNotNull(userRemoteDataSource);
    }

    public static UserRepository getInstance(@NonNull UserDataSource userRemoteDataSource,
                                             @NonNull UserDataSource userLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository(userLocalDataSource, userRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NonNull LoadAllUserCallback callback) {
        checkNotNull(callback);
        if (user!=null){
            List<User> users = new ArrayList<>();
            users.add(user);
            callback.onAllUserLoaded(users);
        }else
            local.getUsers(callback);
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
        local.deleteAllUser();
    }

    @Override
    public void check(@NonNull final String account, @NonNull final String password, @NonNull final Callback callback) {
        checkNotNull(account);
        checkNotNull(password);
        checkNotNull(callback);
        remote.check(account, password, new Callback() {
            @Override
            public void onSuccess(User u) {
                user = u;
                local.saveUser(user);
                callback.onSuccess(user);
            }

            @Override
            public void onFail(String log) {
                callback.onFail(log);
            }
        });
    }

    @Override
    public void register(@NonNull String account, @NonNull String password, @NonNull String email, @NonNull final Callback callback) {
        checkNotNull(account);
        checkNotNull(password);
        checkNotNull(callback);
        remote.register(account, password, email, callback);
    }

    @Override
    public void refreshUsers() {

    }
}
