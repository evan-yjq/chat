package com.evan.chat.data.source.User;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.User.model.User;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import static com.evan.chat.util.GsonUtil.parseJsonWithGson;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午7:49
 */
public class UserRemoteDataSource implements UserDataSource {

    private static UserRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private AppExecutors mAppExecutors;

    public static UserRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (UserRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    private UserRemoteDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
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
    public void register(@NonNull final String account, @NonNull final String password, @NonNull final String email,
                         @NonNull final Callback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://115.28.216.244:3000/user/register")
                        .addParams("account", account).addParams("password", password)
                        .addParams("email", email).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                callback.onFail(e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (!"".equals(response)) {
                                    User user = new User();
                                    user.setId(Long.parseLong(response));
                                    callback.onSuccess(user);
                                } else {
                                    callback.onFail("");
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void check(@NonNull final String account, @NonNull final String password, @NonNull final Callback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url(PropertiesUtils.getInstance().getProperty("sign_in", true))
                        .addParams("account", account).addParams("password", password).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                callback.onFail(e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (!"".equals(response)) {
                                    callback.onSuccess(parseJsonWithGson(response, User.class));
                                } else {
                                    callback.onFail("");
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void refreshUsers() {

    }
}
