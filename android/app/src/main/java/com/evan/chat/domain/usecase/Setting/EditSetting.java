package com.evan.chat.domain.usecase.Setting;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Settings.SettingsRepository;
import com.evan.chat.data.source.model.Setting;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/30
 * Time: 下午6:16
 */
public class EditSetting extends UseCase<EditSetting.RequestValues,EditSetting.ResponseValue> {

    private final SettingsRepository settingsRepository;

    public EditSetting(@NonNull SettingsRepository settingsRepository) {
        this.settingsRepository = checkNotNull(settingsRepository,"settingsRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        final Setting setting = requestValues.getSetting();
        settingsRepository.editSetting(setting);
        getUseCaseCallback().onSuccess(new ResponseValue(setting));
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final Setting setting;


        public RequestValues(@NonNull Setting setting) {
            this.setting = checkNotNull(setting,"setting cannot be null!");
        }

        public Setting getSetting() {
            return setting;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private final Setting setting;

        public ResponseValue(@NonNull Setting setting) {
            this.setting = checkNotNull(setting,"setting cannot be null!");
        }

        public Setting getSetting() {
            return setting;
        }
    }

}
