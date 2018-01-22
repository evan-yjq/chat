package com.evan.chat.bus;

import com.evan.chat.json.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/6
 * Time: 22:40
 */
public class UserBus {
    private Map<String,UseUserBus> use_bus_activity = new HashMap<>();
    private UserInfo userInfo = null;
    private static UserBus userBus = null;
    
    public static UserBus init(){
        if (userBus==null) {
            userBus = new UserBus();
        }
        return userBus;
    }
    
    public void add_activity(String k,UseUserBus c){
        use_bus_activity.put(k, c);
    }
    
    public UseUserBus get_activity(String k){
        return use_bus_activity.get(k);
    }

    public void remove_activity(Object o){
        use_bus_activity.remove(o);
    }
    
    public void update_user_info(UserInfo userInfo){
        this.userInfo = userInfo;
        for (UseUserBus value : use_bus_activity.values()) {
            value.user_update(userInfo);
        }
    }
}
