package com.evan.chat.data.source.Settings;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.Setting;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/30
 * Time: 下午6:24
 */
public interface SettingsDataSource {

    interface LoadSettingsCallback{

        void onSettingsLoaded(List<Setting> settings);

        void onDataNotAvailable();
    }

    interface GetSettingCallback{

        void onSettingLoaded(Setting setting);

        void onDataNotAvailable();
    }

    void getSetting(@NonNull Long id, @NonNull GetSettingCallback callback);

    void saveSetting(@NonNull Setting setting);

    void deleteSetting(@NonNull Long id);

    void deleteAllSetting();

    void getSettings(@NonNull LoadSettingsCallback callback);

    void editSetting(@NonNull Setting setting);

    void refreshSettings();
}
