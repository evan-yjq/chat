package com.evan.chat.settings;

import android.support.annotation.NonNull;
import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;
import com.evan.chat.data.source.model.Setting;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/28
 * Time: 下午5:14
 */
public interface SettingsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showSettings(List<Setting> settings);

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter {

        void edit(@NonNull Setting setting);

        void loadSettings(boolean forceUpdate);

    }
}
