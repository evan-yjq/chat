package com.evan.chat.data.source.Settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.evan.chat.data.source.model.Setting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/30
 * Time: 下午7:11
 */
public class SettingsRepository implements SettingsDataSource{

    private static SettingsRepository INSTANCE = null;

    private final SettingsDataSource mSettingsRemoteDataSource;

    private final SettingsDataSource mSettingsLocalDataSource;

    private Map<Long,Setting> mCachedSettings;

    private boolean mCacheIsDirty = false;

    private SettingsRepository(@NonNull SettingsDataSource settingsLocalDataSource,
                               @NonNull SettingsDataSource settingsRemoteDataSource){
        mSettingsLocalDataSource = settingsLocalDataSource;
        mSettingsRemoteDataSource = settingsRemoteDataSource;
    }

    public static SettingsRepository getInstance(@NonNull SettingsDataSource settingsRemoteDataSource,
                                                 @NonNull SettingsDataSource settingsLocalDataSource){
        if (INSTANCE == null){
            INSTANCE = new SettingsRepository(settingsLocalDataSource, settingsRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getSetting(@NonNull final Long id, @NonNull final GetSettingCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);

        // 因为在进入设置界面前有过加载设置操作，
        // 所以为了不弄脏缓存，删除插入缓存的操作

        Setting setting = getSettingWithId(id);

        if (setting!=null){
            callback.onSettingLoaded(setting);
            return;
        }

        mSettingsLocalDataSource.getSetting(id, new GetSettingCallback() {
            @Override
            public void onSettingLoaded(Setting setting) {
                callback.onSettingLoaded(setting);
            }

            @Override
            public void onDataNotAvailable() {
                getSettingsFromRemoteDataSource(new LoadSettingsCallback() {
                    @Override
                    public void onSettingsLoaded(List<Setting> settings) {
                        for (Setting setting : settings) {
                            if (setting.getId().equals(id))
                                callback.onSettingLoaded(setting);
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveSetting(@NonNull final Setting setting) {
        checkNotNull(setting);
        mSettingsRemoteDataSource.saveSetting(setting);
        mSettingsLocalDataSource.saveSetting(setting);
        if (mCachedSettings == null){
            mCachedSettings = new LinkedHashMap<>();
        }
        mCachedSettings.put(setting.getId(),setting);
    }

    @Override
    public void deleteSetting(@NonNull Long id) {
        checkNotNull(id);
        mSettingsLocalDataSource.deleteSetting(id);
        mSettingsRemoteDataSource.deleteSetting(id);
        if (mCachedSettings == null){
            mCachedSettings = new LinkedHashMap<>();
        }
        mCachedSettings.remove(id);
    }

    @Override
    public void deleteAllSetting() {
        mSettingsRemoteDataSource.deleteAllSetting();
        mSettingsLocalDataSource.deleteAllSetting();
        if (mCachedSettings == null){
            mCachedSettings = new LinkedHashMap<>();
        }
        mCachedSettings.clear();
    }

    @Override
    public void getSettings(@NonNull final LoadSettingsCallback callback) {
        checkNotNull(callback);

        if (mCachedSettings != null && !mCacheIsDirty){
            callback.onSettingsLoaded(new ArrayList<>(mCachedSettings.values()));
            return;
        }

        if (mCacheIsDirty){
            getSettingsFromRemoteDataSource(callback);
        }else{
            mSettingsLocalDataSource.getSettings(new LoadSettingsCallback() {
                @Override
                public void onSettingsLoaded(List<Setting> settings) {
                    refreshCache(settings);
                    callback.onSettingsLoaded(new ArrayList<>(mCachedSettings.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getSettingsFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getSettingsFromRemoteDataSource(final LoadSettingsCallback callback){
        mSettingsRemoteDataSource.getSettings(new LoadSettingsCallback() {
            @Override
            public void onSettingsLoaded(List<Setting> settings) {
                refreshCache(settings);
                refreshLocalDataSource(settings);
                callback.onSettingsLoaded(new ArrayList<>(mCachedSettings.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Setting> settings){
        mSettingsLocalDataSource.deleteAllSetting();
        for (final Setting setting : settings) {
            mSettingsLocalDataSource.saveSetting(setting);
        }
    }

    private void refreshCache(List<Setting> settings){
        if (mCachedSettings == null){
            mCachedSettings = new LinkedHashMap<>();
        }
        mCachedSettings.clear();
        for (Setting setting: settings) {
            mCachedSettings.put(setting.getId(),setting);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void editSetting(@NonNull Setting setting) {
        checkNotNull(setting);
        mSettingsLocalDataSource.editSetting(setting);
        mSettingsRemoteDataSource.editSetting(setting);

        Setting s = new Setting(setting);
        if (mCachedSettings == null){
            mCachedSettings = new LinkedHashMap<>();
        }
        mCachedSettings.put(setting.getId(),s);
    }

    @Nullable
    private Setting getSettingWithId(@NonNull Long id) {
        checkNotNull(id);
        if (mCachedSettings == null || mCachedSettings.isEmpty()) {
            return null;
        } else {
            return mCachedSettings.get(id);
        }
    }

    @Override
    public void refreshSettings() {
        mCacheIsDirty = true;
    }
}
