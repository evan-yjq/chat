package com.evan.chat.friends;

import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.data.source.model.User;
import com.evan.chat.data.source.model.setHeadable;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:50
 */
public interface FriendsContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showNoFriends();

        void showFriends(List<Friend>friends);

        void showMessage(String msg);

        void showPopUpMenu();

        void showAddFriends();
    }

    interface Presenter extends BasePresenter{

        void loadFriends(boolean forceUpdate);
    }
}
