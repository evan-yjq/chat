package com.evan.chat.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.Friend.FriendDataSource;
import com.evan.chat.data.source.Friend.FriendRepository;
import com.evan.chat.data.source.model.Friend;

import java.util.List;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 15:05
 */
public class GetFriends extends UseCase<GetFriends.RequestValues,GetFriends.ResponseValue>{

    private final FriendRepository friendRepository;
    private final GetHead getHead;
    private final UseCaseHandler handler;

    public GetFriends(@NonNull GetHead getHead, @NonNull FriendRepository friendRepository,
                      @NonNull UseCaseHandler handler){
        this.friendRepository = checkNotNull(friendRepository,"friendRepository cannot be null!");
        this.getHead = checkNotNull(getHead,"getHead cannot be null!");
        this.handler = checkNotNull(handler,"handler cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        if (requestValues.isForceUpdate()){
            friendRepository.refreshFriends();
        }
        Long id = requestValues.getId();
        friendRepository.getFriends(id, new FriendDataSource.LoadAllFriendsCallback() {
            @Override
            public void onAllFriendLoaded(List<Friend> friends) {
                i=0;
                getHead(friends);
                getUseCaseCallback().onSuccess(new ResponseValue(friends));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }
    private int i;
    private void getHead(final List<Friend> friends){
        if (i==friends.size())return;
        handler.execute(getHead, new GetHead.RequestValues(friends.get(i)),
                new UseCaseCallback<GetHead.ResponseValue>() {
                    @Override
                    public void onSuccess(GetHead.ResponseValue response) {
                        i++;
                        getHead(friends);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Long id;

        private final boolean mForceUpdate;

        public RequestValues(boolean forceUpdate, @NonNull Long id){
            mForceUpdate = forceUpdate;
            this.id = checkNotNull(id,"id cannot be null!");
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public Long getId() {
            return id;
        }
    }
    public static final class ResponseValue implements UseCase.ResponseValue {
        private final List<Friend>friends;

        public ResponseValue(List<Friend>friends){
            this.friends = friends;
        }

        public List<Friend> getFriends() {
            return friends;
        }
    }

}
