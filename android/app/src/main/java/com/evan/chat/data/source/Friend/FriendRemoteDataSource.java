package com.evan.chat.data.source.Friend;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.domain.usecase.GetHead;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.io.File;

import static com.evan.chat.util.GsonUtil.parseJsonArrayWithGson;
import static com.evan.chat.PublicData.user;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:36
 */
public class FriendRemoteDataSource implements FriendDataSource {

    private static FriendRemoteDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    private FriendRemoteDataSource(@NonNull AppExecutors appExecutors){
        mAppExecutors = appExecutors;
    }

    public static FriendRemoteDataSource getInstance(@NonNull AppExecutors appExecutors){
        if (INSTANCE == null){
            synchronized (FriendRemoteDataSource.class){
                if (INSTANCE == null){
                    INSTANCE = new FriendRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getFriends(@NonNull final LoadAllFriendsCallback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url(PropertiesUtils.getInstance().getProperty("get_friends", true))
                        .addParams("id",user.getId().toString()).build()
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
                                    callback.onAllFriendLoaded(parseJsonArrayWithGson(s,Friend.class));
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
    public void saveHead(@NonNull File file, @NonNull Long id, @NonNull Bitmap bitmap) {

    }

    @Override
    public void getHead(@NonNull File file, @NonNull final Long id, @NonNull final HeadCallback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String url = PropertiesUtils.getInstance().getProperty("get_head",true) + id + "/head.png";
                OkHttpUtils.get().url(url)
                        .build().execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail();
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        callback.onSuccess(response);
                    }
                });
            }
        });
    }

    @Override
    public void saveFriend(@NonNull Friend friend) {

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
