package com.evan.chat.follows.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Follow.FollowDataSource;
import com.evan.chat.data.source.Follow.FollowRepository;
import com.evan.chat.data.source.Follow.model.Follow;

import java.util.List;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 15:05
 */
public class GetFollows extends UseCase<GetFollows.RequestValues,GetFollows.ResponseValue>{

    private final FollowRepository friendRepository;

    public GetFollows(@NonNull FollowRepository friendRepository){
        this.friendRepository = checkNotNull(friendRepository,"friendRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        if (requestValues.isForceUpdate()){
            friendRepository.refreshFriends();
        }
        Long id = requestValues.getId();
        friendRepository.getFriends(id, new FollowDataSource.LoadAllFriendsCallback() {
            @Override
            public void onAllFriendLoaded(List<Follow> follows) {
                getUseCaseCallback().onSuccess(new ResponseValue(follows));
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
        private final List<Follow> follows;

        public ResponseValue(List<Follow> follows){
            this.follows = follows;
        }

        public List<Follow> getFollows() {
            return follows;
        }
    }

}
