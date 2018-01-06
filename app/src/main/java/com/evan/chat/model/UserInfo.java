package com.evan.chat.model;

import com.evan.chat.gen.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/6
 * Time: 22:55
 */
public class UserInfo {
    private int id;
    private String username;
    private String profile;
    private List<User>friends;

    public UserInfo(int id,String username,String profile,List<User>friends){
        this.friends = friends;
        this.id = id;
        this.profile = profile;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
