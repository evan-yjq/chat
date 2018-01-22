package com.evan.chat.bus;

import android.content.Context;
import com.evan.chat.json.UserInfo;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/6
 * Time: 23:13
 */
public interface UseUserBus {
    void user_update(UserInfo userInfo);
    Context get_context();
}
