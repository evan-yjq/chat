package com.evan.chat.follows;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.follows.domain.usecase.GetFollows;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:51
 */
public class FollowsPresenter implements FollowsContract.Presenter{

    private final FollowsContract.View view;
    private boolean mFirstStart = true;

    private final GetFollows getFollows;
    private final UseCaseHandler mUseCaseHandler;
    private final Long userId;

    public FollowsPresenter(@NonNull FollowsContract.View view, @NonNull UseCaseHandler mUseCaseHandler,
                            @NonNull GetFollows getFollows, Long userId){
        this.view = checkNotNull(view,"view cannot be null!");
        this.mUseCaseHandler = checkNotNull(mUseCaseHandler,"mUseCaseHandler cannot be null!");
        this.getFollows = checkNotNull(getFollows,"getFollows cannot be null!");
        this.userId = checkNotNull(userId,"userId cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {
       loadFriends(false);
    }

    @Override
    public void loadFriends(boolean forceUpdate){
        loadFriends(forceUpdate,true);
        mFirstStart = false;
    }

    private void loadFriends(boolean forceUpdate, final boolean showLoadingUI){
        if (showLoadingUI){
            view.setLoadingIndicator(true);
        }

        mUseCaseHandler.execute(getFollows, new GetFollows.RequestValues(forceUpdate, userId),
                new UseCase.UseCaseCallback<GetFollows.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFollows.ResponseValue response) {
                        if (view.isActive()){
                            view.showFriends(response.getFollows());
                        }
                        if (showLoadingUI){
                            view.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (view.isActive()){
                            view.showNoFriends();
                        }
                        if (showLoadingUI){
                            view.setLoadingIndicator(false);
                        }
                    }
                });
    }
}
