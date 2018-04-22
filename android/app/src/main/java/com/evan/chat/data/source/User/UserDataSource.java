package com.evan.chat.data.source.User;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午7:48
 */
public interface UserDataSource {

    interface LoadAllUserCallback{

        void onAllUserLoaded(List<User> users);

        void onDataNotAvailable();
    }

    interface LoadUserCallback{

        void onUserLoaded(User user);

        void onDataNotAvailable();
    }

    interface Callback{

        void onSuccess(User user);

        void onFail(String log);
    }

    void getUsers(@NonNull LoadAllUserCallback callback);

    void getUser(@NonNull Long id, LoadUserCallback callback);

    void saveUser(@NonNull User user);

    void deleteUser(@NonNull Long id);

    void deleteAllUser();

    void check(@NonNull String account, @NonNull String password, @NonNull Callback callback);

    void register(@NonNull String account, @NonNull String password, @NonNull String email, @NonNull Callback callback);

    void refreshUsers();
}
