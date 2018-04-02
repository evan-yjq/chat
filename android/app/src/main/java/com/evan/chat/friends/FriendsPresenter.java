package com.evan.chat.friends;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.friends.domain.usecase.GetFriends;
import com.evan.chat.util.AppExecutors;
import com.zhy.http.okhttp.OkHttpUtils;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:51
 */
public class FriendsPresenter implements FriendsContract.Presenter{

    private final FriendsContract.View view;
    private boolean mFirstStart = true;

    private final GetFriends getFriends;
    private final UseCaseHandler mUseCaseHandler;
    private final Long userId;

    public FriendsPresenter(@NonNull FriendsContract.View view, @NonNull UseCaseHandler mUseCaseHandler,
                            @NonNull GetFriends getFriends, Long userId){
        this.view = checkNotNull(view,"view cannot be null!");
        this.mUseCaseHandler = checkNotNull(mUseCaseHandler,"mUseCaseHandler cannot be null!");
        this.getFriends = checkNotNull(getFriends,"getFriends cannot be null!");
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

        mUseCaseHandler.execute(getFriends, new GetFriends.RequestValues(forceUpdate, userId),
                new UseCase.UseCaseCallback<GetFriends.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFriends.ResponseValue response) {
                        if (view.isActive()){
                            view.showFriends(response.getFriends());
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
