package com.evan.chat.domain.usecase.User;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.data.source.User.UserDataSource;
import com.evan.chat.data.source.User.UserRepository;
import com.evan.chat.data.source.model.User;
import com.evan.chat.domain.usecase.GetHead;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/27
 * Time: 下午1:59
 */
public class SignInUser extends UseCase<SignInUser.RequestValues,SignInUser.ResponseValue>{

    private final UserRepository userRepository;
    private final GetHead getHead;
    private final UseCaseHandler handler;

    public SignInUser(@NonNull GetHead getHead, @NonNull UserRepository userRepository,
                      @NonNull UseCaseHandler handler){
        this.userRepository = checkNotNull(userRepository,"userRepository cannot be null!");
        this.getHead = checkNotNull(getHead,"getHead cannot be null!");
        this.handler = checkNotNull(handler,"handler cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String account = requestValues.getAccount();
        String password = requestValues.getPassword();
        userRepository.check(account, password, new UserDataSource.Callback() {
            @Override
            public void onSuccess(final User user) {
                handler.execute(getHead, new GetHead.RequestValues(user),
                        new UseCaseCallback<GetHead.ResponseValue>() {
                            @Override
                            public void onSuccess(GetHead.ResponseValue response) {
                                getUseCaseCallback().onSuccess(new ResponseValue(user));
                            }

                            @Override
                            public void onError() {

                            }
                        });
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
