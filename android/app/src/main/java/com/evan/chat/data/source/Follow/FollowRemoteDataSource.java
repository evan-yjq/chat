package com.evan.chat.data.source.Follow;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.Follow.model.Follow;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import static com.evan.chat.util.GsonUtil.parseJsonArrayWithGson;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:36
 */
public class FollowRemoteDataSource implements FollowDataSource {

    private static FollowRemoteDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    private FollowRemoteDataSource(@NonNull AppExecutors appExecutors){
        mAppExecutors = appExecutors;
    }

    public static FollowRemoteDataSource getInstance(@NonNull AppExecutors appExecutors){
        if (INSTANCE == null){
            synchronized (FollowRemoteDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new FollowRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getFriends(@NonNull final Long id, @NonNull final LoadAllFriendsCallback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url(PropertiesUtils.getInstance().getProperty("get_friends", true))
                        .addParams("id",id.toString()).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                callback.onDataNotAvailable();
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                if ("".equals(s)){
                                    callback.onDataNotAvailable();
                                }else{
                                    callback.onAllFriendLoaded(parseJsonArrayWithGson(s,Follow.class));
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void getFriend(@NonNull Long id, @NonNull LoadFriendCallback callback) {

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
