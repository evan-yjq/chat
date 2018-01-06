package com.evan.chat.gen;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    //用户id
    @Id(autoincrement = true)
    private Long id;
    private int user_id;
    //用户名
    private String username;
    //个人简介
    private String profile;


    @Generated(hash = 37538486)
    public User(Long id, int user_id, String username, String profile) {
        this.id = id;
        this.user_id = user_id;
        this.username = username;
        this.profile = profile;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
}
