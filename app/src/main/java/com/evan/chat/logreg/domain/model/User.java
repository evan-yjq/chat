package com.evan.chat.logreg.domain.model;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:37
 */
@Entity
public class User {

    //用户id
    private Long id;
    //用户名
    private String username;
    private String password;
    private Date login_time;
    //个人简介
    private String profile;

    public User(Long id, String username, String password, String profile) {
        this(id,username,password,new Date(),profile);
    }

    @Generated(hash = 2016033953)
    public User(Long id, String username, String password, Date login_time,
            String profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.login_time = login_time;
        this.profile = profile;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", login_time=" + login_time +
                ", profile='" + profile + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(login_time, user.login_time) &&
                Objects.equals(profile, user.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, login_time, profile);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Date login_time) {
        this.login_time = login_time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
