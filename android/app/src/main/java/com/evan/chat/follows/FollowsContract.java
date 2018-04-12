package com.evan.chat.follows;

import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;
import com.evan.chat.data.source.Follow.model.Follow;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:50
 */
public interface FollowsContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showNoFriends();
        void showFriends(List<Follow> follows);
        void showAddFriends();
    }

    interface Presenter extends BasePresenter{
        void loadFriends(boolean forceUpdate);
    }
}
