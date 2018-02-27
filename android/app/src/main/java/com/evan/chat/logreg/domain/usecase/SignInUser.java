package com.evan.chat.logreg.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.User.UserDataSource;
import com.evan.chat.data.source.User.UserRepository;
import com.evan.chat.data.source.User.model.User;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/27
 * Time: 下午1:59
 */
public class SignInUser extends UseCase<SignInUser.RequestValues,SignInUser.ResponseValue>{

    private final UserRepository userRepository;

    public SignInUser(@NonNull UserRepository userRepository){
        this.userRepository = checkNotNull(userRepository,"userRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String account = requestValues.getAccount();
        String password = requestValues.getPassword();
        userRepository.check(account, password, new UserDataSource.CheckCallback() {
            @Override
            public void onCheckSuccess(User user) {
                getUseCaseCallback().onSuccess(new ResponseValue(user));
            }

            @Override
            public void onCheckFail(String log) {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String account;
        private final String password;

        public RequestValues(@NonNull String account, @NonNull String password) {
            this.account = checkNotNull(account,"account cannot be null!");
            this.password = checkNotNull(password,"password cannot be null!");
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
