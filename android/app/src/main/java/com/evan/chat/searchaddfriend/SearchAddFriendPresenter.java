package com.evan.chat.searchaddfriend;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.domain.usecase.SearchInAllUser;

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

    public SearchAddFriendPresenter(@NonNull SearchAddFriendContract.View view,
                                    @NonNull UseCaseHandler mUseCaseHandler,
                                    @NonNull SearchInAllUser searchInAllUser) {
        this.view = checkNotNull(view,"view cannot be null!");
        this.mUseCaseHandler = checkNotNull(mUseCaseHandler,"mUseCaseHandler cannot be null!");
        this.searchInAllUser = checkNotNull(searchInAllUser,"searchInAllUser cannot be null!");
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    public void search(String key){
        view.setLoadingIndicator(true);
        mUseCaseHandler.execute(searchInAllUser, new SearchInAllUser.RequestValues(key),
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
}
