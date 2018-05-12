package com.evan.chat.searchaddfriend;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.domain.usecase.Friend.AddFriend;
import com.evan.chat.domain.usecase.Friend.SearchInAllUser;

import java.util.ArrayList;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/26
 * Time: 13:22
 */
public class SearchAddFriendPresenter implements SearchAddFriendContract.Presenter {

    private final SearchAddFriendContract.View view;
    private final UseCaseHandler mUseCaseHandler;
    private final SearchInAllUser searchInAllUser;
    private final AddFriend addFriend;

    public SearchAddFriendPresenter(@NonNull SearchAddFriendContract.View view,
                                    @NonNull UseCaseHandler mUseCaseHandler,
                                    @NonNull SearchInAllUser searchInAllUser,
                                    @NonNull AddFriend addFriend) {
        this.view = checkNotNull(view,"view cannot be null!");
        this.mUseCaseHandler = checkNotNull(mUseCaseHandler,"mUseCaseHandler cannot be null!");
        this.searchInAllUser = checkNotNull(searchInAllUser,"searchInAllUser cannot be null!");
        this.addFriend = checkNotNull(addFriend,"addFriend cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    public void search(String key){
        view.setLoadingIndicator(true);
        mUseCaseHandler.execute(searchInAllUser, new SearchInAllUser.RequestValues(key, view.getFile()),
                new UseCase.UseCaseCallback<SearchInAllUser.ResponseValue>() {
                    @Override
                    public void onSuccess(SearchInAllUser.ResponseValue response) {
                        if (view.isActive()) {
                            view.showFriends(response.getFriends());
                            view.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (view.isActive()){
                            view.setLoadingIndicator(false);
                        }
                    }
                });
    }

    @Override
    public void addFriend(Friend friend) {
        view.setLoadingIndicator(true);
        mUseCaseHandler.execute(addFriend, new AddFriend.RequestValues(friend),
                new UseCase.UseCaseCallback<AddFriend.ResponseValue>() {
                    @Override
                    public void onSuccess(AddFriend.ResponseValue response) {
                        if (view.isActive()) {
                            view.setLoadingIndicator(false);
                            view.addFriendSuccess();
                        }
                    }

                    @Override
                    public void onError() {
                        if (view.isActive()){
                            view.setLoadingIndicator(false);
                            view.addFriendFail();
                        }
                    }
                });
    }
}
