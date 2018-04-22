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
import com.evan.chat.domain.usecase.*;
import com.evan.chat.data.source.Chat.ChatLocalDataSource;
import com.evan.chat.data.source.Chat.ChatRemoteDataSource;
import com.evan.chat.data.source.Chat.ChatRepository;
import com.evan.chat.data.source.Friend.FriendLocalDataSource;
import com.evan.chat.data.source.Friend.FriendRemoteDataSource;
import com.evan.chat.data.source.Friend.FriendRepository;
import com.evan.chat.data.source.User.UserLocalDataSource;
import com.evan.chat.data.source.User.UserRemoteDataSource;
import com.evan.chat.data.source.User.UserRepository;
import com.evan.chat.data.source.dao.DaoSession;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.GreenDaoUtils;
import com.evan.chat.welcome.domain.usecase.GetAutoUser;

import static com.evan.chat.util.Objects.checkNotNull;


public class Injection {

    private static UserRepository provideUserRepository(@NonNull Context context) {
        checkNotNull(context);
        DaoSession database = GreenDaoUtils.getSingleTon().getmDaoSession(context);
        return UserRepository.getInstance(UserRemoteDataSource.getInstance(new AppExecutors()),
                UserLocalDataSource.getInstance(new AppExecutors(),
                        database.getUserDao()));
    }

    private static FriendRepository provideFriendRepository(@NonNull Context context) {
        checkNotNull(context);
        DaoSession database = GreenDaoUtils.getSingleTon().getmDaoSession(context);
        return FriendRepository.getInstance(FriendRemoteDataSource.getInstance(new AppExecutors()),
                FriendLocalDataSource.getInstance(new AppExecutors(),
                        database.getFriendDao()));
    }

    private static ChatRepository provideChatRepository(@NonNull Context context) {
        checkNotNull(context);
        DaoSession database = GreenDaoUtils.getSingleTon().getmDaoSession(context);
        return ChatRepository.getInstance(ChatRemoteDataSource.getInstance(new AppExecutors()),
                ChatLocalDataSource.getInstance(new AppExecutors(),
                        database.getChatDao()));
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static RegisterUser provideRegUser(@NonNull Context context){
        return new RegisterUser(provideUserRepository(context));
    }

    public static SignInUser provideSignInUser(@NonNull Context context){
        return new SignInUser(provideGetHead(), provideUserRepository(context), provideUseCaseHandler());
    }

    public static GetFriends provideGetFriends(@NonNull Context context){
        return new GetFriends(provideGetHead(), provideFriendRepository(context), provideUseCaseHandler());
    }

    public static GetHead provideGetHead(){
        return new GetHead();
    }

    public static SendMessage provideSendMessage(@NonNull Context context){
        return new SendMessage(provideChatRepository(context));
    }

    public static GetAutoUser provideGetAutoUser(@NonNull Context context){
        return new GetAutoUser(provideUserRepository(context));
    }

    public static DeleteAllUser provideDeleteAllUser(@NonNull Context context){
        return new DeleteAllUser(provideUserRepository(context));
    }
}
