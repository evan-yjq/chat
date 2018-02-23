package com.evan.chat.data.source.User;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.User.model.User;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午7:49
 */
public class UserRemoteDataSource implements UserDataSource {
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
    public void check(@NonNull String account, @NonNull String password, @NonNull CheckCallback callback) {

    }

    @Override
    public void refreshUsers() {

    }
}
