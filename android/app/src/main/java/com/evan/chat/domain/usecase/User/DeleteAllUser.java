package com.evan.chat.domain.usecase.User;

import android.support.annotation.NonNull;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.User.UserRepository;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:20
 */
public class DeleteAllUser extends UseCase<DeleteAllUser.RequestValues, DeleteAllUser.ResponseValue>{

    private final UserRepository userRepository;

    public DeleteAllUser(@NonNull UserRepository userRepository) {
        this.userRepository = checkNotNull(userRepository,"userRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        userRepository.deleteAllUser();
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {

    }
    public static final class ResponseValue implements UseCase.ResponseValue {

    }


}
