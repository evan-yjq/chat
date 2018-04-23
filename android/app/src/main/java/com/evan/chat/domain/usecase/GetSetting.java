package com.evan.chat.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Settings.SettingsDataSource;
import com.evan.chat.data.source.Settings.SettingsRepository;
import com.evan.chat.data.source.model.Setting;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/30
 * Time: 下午10:39
 */
public class GetSetting extends UseCase<GetSetting.RequestValues,GetSetting.ResponseValue>{

    private final SettingsRepository settingsRepository;

    public GetSetting(@NonNull SettingsRepository settingsRepository) {
        this.settingsRepository = checkNotNull(settingsRepository,"settingsRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        final Long id = requestValues.getId();
        settingsRepository.getSetting(id, new SettingsDataSource.GetSettingCallback() {
            @Override
            public void onSettingLoaded(Setting setting) {
                getUseCaseCallback().onSuccess(new ResponseValue(setting));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Long id;

        public RequestValues(@NonNull Long id) {
            this.id = checkNotNull(id,"id cannot be null!");
        }

        public Long getId() {
            return id;
        }
    }
    public static final class ResponseValue implements UseCase.ResponseValue {
        private final Setting setting;

        public ResponseValue(@NonNull Setting setting) {
            this.setting = checkNotNull(setting,"setting cannot be null!");
        }

        public Setting getSetting() {
            return setting;
        }
    }
}
