package com.evan.chat.data.source.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.evan.chat.data.source.User.model.User;
import com.evan.chat.gen.Chat;
import com.evan.chat.data.source.Friend.model.Friend;

import com.evan.chat.data.source.dao.UserDao;
import com.evan.chat.data.source.dao.ChatDao;
import com.evan.chat.data.source.dao.FriendDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig chatDaoConfig;
    private final DaoConfig friendDaoConfig;

    private final UserDao userDao;
    private final ChatDao chatDao;
    private final FriendDao friendDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        chatDaoConfig = daoConfigMap.get(ChatDao.class).clone();
        chatDaoConfig.initIdentityScope(type);

        friendDaoConfig = daoConfigMap.get(FriendDao.class).clone();
        friendDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        chatDao = new ChatDao(chatDaoConfig, this);
        friendDao = new FriendDao(friendDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Chat.class, chatDao);
        registerDao(Friend.class, friendDao);
    }
    
    public void clear() {
        userDaoConfig.clearIdentityScope();
        chatDaoConfig.clearIdentityScope();
        friendDaoConfig.clearIdentityScope();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ChatDao getChatDao() {
        return chatDao;
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

}
