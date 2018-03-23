package com.evan.chat.logreg.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.User.UserDataSource;
import com.evan.chat.data.source.User.UserRepository;
import com.evan.chat.data.source.User.model.User;

import static com.evan.chat.util.Objects.checkNotNull;

public class RegisterUser extends UseCase<RegisterUser.RequestValues,RegisterUser.ResponseValue> {

    private final UserRepository userRepository;

    public RegisterUser(@NonNull UserRepository userRepository){
        this.userRepository = checkNotNull(userRepository,"userRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String account = requestValues.getAccount();
        String password = requestValues.getPassword();
        String email = requestValues.getEmail();
        userRepository.register(account, password, email, new UserDataSource.Callback() {

            @Override
            public void onSuccess(User user) {
                getUseCaseCallback().onSuccess(new ResponseValue(user));
            }

            @Override
            public void onFail(String log) {
                getUseCaseCallback().onError();
            }
        });
    }


    public static final class RequestValues implements UseCase.RequestValues {
        private final String account;
        private final String password;
        private final String email;

        public RequestValues(@NonNull String account, @NonNull String password, @NonNull String email) {
            this.account = checkNotNull(account,"account cannot be null!");
            this.password = checkNotNull(password,"password cannot be null!");
            this.email = checkNotNull(email,"email cannot be null!");
        }

        public String getEmail() {
            return email;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final User user;

        public ResponseValue(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
