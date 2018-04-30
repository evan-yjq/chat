package com.evan.chat.searchaddfriend;

import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;
import com.evan.chat.data.source.model.Friend;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/26
 * Time: 13:21
 */
public interface SearchAddFriendContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(final boolean active);

        void showMessage(String message);

        void showFriends(List<Friend> friends);

        void addFriendSuccess();
    }

    interface Presenter extends BasePresenter{

        void addFriend(Friend friend);
    }
}
