package com.evan.chat.data.source.Settings;

import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.Setting;
import com.evan.chat.settings.domain.SettingDisplay;
import com.evan.chat.settings.domain.SettingKey;
import com.evan.chat.util.Objects;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/30
 * Time: 下午6:45
 */
public class SettingsRemoteDataSource implements SettingsDataSource{

    private static SettingsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<Long,Setting> SETTINGS_SERVICE_DATA;

    static {
        SETTINGS_SERVICE_DATA = new LinkedHashMap<>(1);
        addSetting(SettingKey.FACE_LOGIN_OPEN_ID,"人脸登陆",
                "false", "重启应用后生效", SettingDisplay.CAPTION_SWITCH_ITEM);
        addSetting(SettingKey.AUTO_LOGIN_OPEN_ID, "自动登陆",
                "true", "", SettingDisplay.SWITCH_ITEM);
    }

    private static void addSetting(Long id, String key, String value, String caption, int display){
        Setting setting = new Setting(id, key, value, caption, display);
        SETTINGS_SERVICE_DATA.put(id,setting);
    }

    public static SettingsRemoteDataSource getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SettingsRemoteDataSource();
        }
        return INSTANCE;
    }

    private SettingsRemoteDataSource(){}

    @Override
    public void getSetting(@NonNull Long id, @NonNull GetSettingCallback callback) {

    }

    @Override
    public void saveSetting(@NonNull Setting setting) {

    }

    @Override
    public void deleteSetting(@NonNull Long id) {

    }

    @Override
    public void deleteAllSetting() {

    }

    @Override
    public void getSettings(@NonNull LoadSettingsCallback callback) {
        callback.onSettingsLoaded(Objects.Lists.newArrayList(SETTINGS_SERVICE_DATA.values()));
    }

    @Override
    public void editSetting(@NonNull Setting setting) {

    }

    @Override
    public void refreshSettings() {
        //
    }
}
