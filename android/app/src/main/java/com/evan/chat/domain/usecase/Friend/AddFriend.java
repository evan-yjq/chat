package com.evan.chat.domain.usecase.Friend;

import android.support.annotation.NonNull;
import com.evan.chat.PublicData;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Friend.FriendRepository;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: evan
 * Date: 18-4-30
 * Time: 下午7:28
 */
public class AddFriend extends UseCase<AddFriend.RequestValues, AddFriend.ResponseValue> {

    private final FriendRepository friendRepository;
    private final AppExecutors appExecutors;

    public AddFriend(@NonNull FriendRepository friendRepository) {
        this.appExecutors = new AppExecutors();
        this.friendRepository = checkNotNull(friendRepository,"friendRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        final Friend friend = requestValues.getFriend();
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url(PropertiesUtils.getInstance().getProperty("add_friend",true))
                        .addParams("userId", String.valueOf(PublicData.user.getId()))
                        .addParams("friendId", String.valueOf(friend.getId()))
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        getUseCaseCallback().onError();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if ("ok".equals(s)) {
                            getUseCaseCallback().onSuccess(new ResponseValue());
                            friend.setRelationship("");
                            friendRepository.saveFriend(friend);
                        } else getUseCaseCallback().onError();
                    }
                });
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Friend friend;

        public RequestValues(@NonNull Friend friend) {
            this.friend = checkNotNull(friend);
        }

        public Friend getFriend() {
            return friend;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
