package com.evan.chat.friends.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Friend.FriendDataSource;
import com.evan.chat.data.source.Friend.FriendRepository;
import com.evan.chat.data.source.Friend.model.Friend;

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

    public GetFriends(@NonNull FriendRepository friendRepository){
        this.friendRepository = checkNotNull(friendRepository,"friendRepository cannot be null!");
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
                getUseCaseCallback().onSuccess(new ResponseValue(friends));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
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
