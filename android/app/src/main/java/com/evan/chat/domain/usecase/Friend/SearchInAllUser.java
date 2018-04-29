package com.evan.chat.domain.usecase.Friend;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.domain.usecase.GetHead;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.util.List;

import static com.evan.chat.util.GsonUtil.parseJsonArrayWithGson;
import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/27
 * Time: 13:04
 */
public class SearchInAllUser extends UseCase<SearchInAllUser.RequestValues, SearchInAllUser.ResponseValue>{

    private AppExecutors appExecutors;
    private final GetHead getHead;
    private final UseCaseHandler handler;
    private final GetFriends getFriends;

    public SearchInAllUser(@NonNull GetHead getHead, @NonNull UseCaseHandler handler, @NonNull GetFriends getFriends){
        appExecutors = new AppExecutors();
        this.getHead = checkNotNull(getHead,"getHead cannot be null!");
        this.handler = checkNotNull(handler,"handler cannot be null!");
        this.getFriends = checkNotNull(getFriends,"getFriends cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        final String key = requestValues.getKey();
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url(PropertiesUtils.getInstance().getProperty("search_in_all_user", true))
                        .addParams("keyword", key).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        getUseCaseCallback().onError();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        final List<Friend>friends = parseJsonArrayWithGson(s,Friend.class);
                        j=0;
                        getHead(friends);
                    }
                });
            }
        });
    }

    private void LoadAllSuccess(final List<Friend>friends){
        handler.execute(getFriends, new GetFriends.RequestValues(true),
                new UseCaseCallback<GetFriends.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFriends.ResponseValue response) {
                        List<Friend>nowFriends = response.getFriends();
                        for (Friend nowFriend : nowFriends){
                            for (Friend friend : friends) {
                                if (friend.getId().equals(nowFriend.getId())){
                                    friend.setRelationship("NO");
                                }
                            }
                        }
                        getUseCaseCallback().onSuccess(new ResponseValue(friends));
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private int j;
    private void getHead(final List<Friend> friends){
        if (j>=friends.size()){
            LoadAllSuccess(friends);
            return;
        }
        handler.execute(getHead, new GetHead.RequestValues(friends.get(j)),
                new UseCaseCallback<GetHead.ResponseValue>() {
                    @Override
                    public void onSuccess(GetHead.ResponseValue response) {
                        j++;
                        getHead(friends);
                    }

                    @Override
                    public void onError() {
                        j++;
                        getHead(friends);
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String key;

        public RequestValues(@NonNull String key) {
            this.key = checkNotNull(key, "key cannot be null!");
        }

        public String getKey() {
            return key;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final List<Friend>friends;

        public ResponseValue(List<Friend> friends) {
            this.friends = checkNotNull(friends);
        }

        public List<Friend> getFriends() {
            return friends;
        }
    }
}
