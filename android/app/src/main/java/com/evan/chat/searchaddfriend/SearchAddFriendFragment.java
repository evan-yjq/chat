package com.evan.chat.searchaddfriend;

import android.support.v4.app.Fragment;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/26
 * Time: 13:22
 */
public class SearchAddFriendFragment extends Fragment implements SearchAddFriendContract.View {
    private SearchAddFriendContract.Presenter presenter;

    @Override
    public void setPresenter(SearchAddFriendContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
