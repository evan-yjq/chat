package com.evan.chat.settings;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.model.Setting;
import com.evan.chat.domain.usecase.Setting.EditSetting;
import com.evan.chat.domain.usecase.Setting.GetSettings;

import java.util.List;

import static com.evan.chat.util.Objects.checkNotNull;


/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/28
 * Time: 下午5:18
 */
public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View mSettingsView;
    private final EditSetting mEditSetting;
    private final GetSettings mGetSettings;

    // 设定第一次是否从网络数据中启动
    private boolean mFirstLoad = false;

    private final UseCaseHandler mUseCaseHandler;

    public SettingsPresenter(@NonNull SettingsContract.View mSettingsView,
                             @NonNull EditSetting mEditSetting,
                             @NonNull GetSettings mGetSettings,
                             @NonNull UseCaseHandler mUseCaseHandler) {
        this.mSettingsView = mSettingsView;
        this.mEditSetting = mEditSetting;
        this.mGetSettings = mGetSettings;
        this.mUseCaseHandler = mUseCaseHandler;

        mSettingsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadSettings(false);
    }

    @Override
    public void edit(@NonNull Setting setting) {
        checkNotNull(setting);
        mUseCaseHandler.execute(mEditSetting, new EditSetting.RequestValues(setting),
                new UseCase.UseCaseCallback<EditSetting.ResponseValue>() {
                    @Override
                    public void onSuccess(EditSetting.ResponseValue response) {
                        loadSettings(false,false);
                    }

                    @Override
                    public void onError() {
                        mSettingsView.showEditFail();
                    }
                });
    }

    @Override
    public void loadSettings(boolean forceUpdate) {
        loadSettings(forceUpdate||mFirstLoad,true);
    }

    private void loadSettings(boolean forceUpdate, final boolean showLoadingUI){
        if (showLoadingUI){
            mSettingsView.setLoadingIndicator(true);
        }
        GetSettings.RequestValues requestValues = new GetSettings.RequestValues(forceUpdate);

        mUseCaseHandler.execute(mGetSettings, requestValues,
                new UseCase.UseCaseCallback<GetSettings.ResponseValue>() {
                    @Override
                    public void onSuccess(GetSettings.ResponseValue response) {
                        List<Setting> settings = response.getSettings();
                        if (!mSettingsView.isActive()){
                            return;
                        }
                        if (showLoadingUI){
                            mSettingsView.setLoadingIndicator(false);
                        }
                        mSettingsView.showSettings(settings);
                    }

                    @Override
                    public void onError() {
                        if (!mSettingsView.isActive()){
                            return;
                        }
                        if (showLoadingUI){
                            mSettingsView.setLoadingIndicator(false);
                        }
                        mSettingsView.showLoadFail();
                    }
                });

    }
}
