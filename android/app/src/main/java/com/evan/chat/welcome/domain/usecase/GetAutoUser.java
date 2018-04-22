package com.evan.chat.welcome.domain.usecase;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.User.UserDataSource;
import com.evan.chat.data.source.User.UserRepository;
import com.evan.chat.data.source.model.User;

import java.util.List;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 10:18
 */
public class GetAutoUser extends UseCase<GetAutoUser.RequestValues, GetAutoUser.ResponseValue> {

    private final UserRepository userRepository;

    public GetAutoUser(@NonNull UserRepository userRepository){
        this.userRepository = checkNotNull(userRepository,"userRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        userRepository.getUsers(new UserDataSource.LoadAllUserCallback() {
            @Override
            public void onAllUserLoaded(List<User> users) {
                User user = users.get(users.size()-1);
                getUseCaseCallback().onSuccess(new ResponseValue(user));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

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
