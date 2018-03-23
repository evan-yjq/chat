/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evan.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import com.evan.chat.data.source.User.UserLocalDataSource;
import com.evan.chat.data.source.User.UserRemoteDataSource;
import com.evan.chat.data.source.User.UserRepository;
import com.evan.chat.data.source.dao.DaoSession;
import com.evan.chat.logreg.domain.usecase.RegisterUser;
import com.evan.chat.logreg.domain.usecase.SignInUser;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.GreenDaoUtils;

import static com.evan.chat.util.Objects.checkNotNull;


public class Injection {

    public static UserRepository provideUserRepository(@NonNull Context context) {
        checkNotNull(context);
        DaoSession database = GreenDaoUtils.getSingleTon().getmDaoSession(context);
        return UserRepository.getInstance(UserRemoteDataSource.getInstance(new AppExecutors()),
                UserLocalDataSource.getInstance(new AppExecutors(),
                        database.getUserDao()));
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static RegisterUser provideRegUser(@NonNull Context context){
        return new RegisterUser(provideUserRepository(context));
    }

    public static SignInUser provideSignInUser(@NonNull Context context){
        return new SignInUser(provideUserRepository(context));
    }
}
