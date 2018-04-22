package com.evan.chat.data.source.User;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.User;
import com.evan.chat.data.source.dao.UserDao;
import com.evan.chat.util.AppExecutors;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午7:49
 */
public class UserLocalDataSource implements UserDataSource {

    private static volatile UserLocalDataSource INSTANCE;

    private UserDao mUserDao;

    private AppExecutors mAppExecutors;

    private UserLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull UserDao userDao){
        mAppExecutors = appExecutors;
        mUserDao = userDao;
    }

    public static UserLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull UserDao userDao){
        if (INSTANCE == null){
            synchronized (UserLocalDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new UserLocalDataSource(appExecutors,userDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NonNull final LoadAllUserCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<User> users = mUserDao.loadAll();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (users.isEmpty()){
                            callback.onDataNotAvailable();
                        }else {
                            callback.onAllUserLoaded(users);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getUser(@NonNull final Long id, final LoadUserCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final User user = mUserDao.load(id);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (user == null){
                            callback.onDataNotAvailable();
                        }else {
                            callback.onUserLoaded(user);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void saveUser(@NonNull final User user) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<User> users = mUserDao.loadAll();
                boolean b = false;
                for (User u : users) {
                    if (u.equals(user))
                        b = true;
                }
                if (b) mUserDao.update(user);
                else mUserDao.insert(user);
            }
        });
    }

    @Override
    public void deleteUser(@NonNull final Long id) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mUserDao.deleteByKey(id);
            }
        });
    }

    @Override
    public void deleteAllUser() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mUserDao.deleteAll();
            }
        });
    }

    @Override
    public void check(@NonNull final String account, @NonNull final String password, @NonNull final Callback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final User user = mUserDao.queryBuilder().whereOr(UserDao.Properties.Account.eq(account),UserDao.Properties.Email.eq(account)).unique();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (user == null) {
                            callback.onFail("该账户不存在");
                        }else {
                            if (user.getPassword().equals(password))
                                callback.onSuccess(user);
                            else
                                callback.onFail("账户名或密码错误");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void register(@NonNull String account, @NonNull String password, @NonNull String email, @NonNull Callback callback) {

    }

    @Override
    public void refreshUsers() {

    }
}
