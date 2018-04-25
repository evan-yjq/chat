package com.evan.chat.data.source.model;

import android.graphics.Bitmap;
import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:37
 */
@Entity
public class User implements Serializable, setHeadable {
    private static final long serialVersionUID = 2L;
    @Id
    private Long id;
    private String account;
    private String nickname;
    private String email;
    private String password;
    private Date login_time;
    private String profile;
    private boolean is_bind_face;
    @Transient
    private Bitmap head;

    public User(Long id, String account, String nickname, String email, String password, String profile, boolean is_bind_face) {
        this(id,account,nickname,email,password,new Date(),profile, is_bind_face);
    }

    @Generated(hash = 395626253)
    public User(Long id, String account, String nickname, String email, String password, Date login_time, String profile,
            boolean is_bind_face) {
        this.id = id;
        this.account = account;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.login_time = login_time;
        this.profile = profile;
        this.is_bind_face = is_bind_face;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", login_time=" + login_time +
                ", profile='" + profile + '\'' +
                ", is_bind_face=" + is_bind_face +
                ", head=" + head +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return is_bind_face == user.is_bind_face &&
                Objects.equals(id, user.id) &&
                Objects.equals(account, user.account) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(login_time, user.login_time) &&
                Objects.equals(profile, user.profile) &&
                Objects.equals(head, user.head);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, account, nickname, email, password, login_time, profile, is_bind_face, head);
    }

    @Override
    public Bitmap getHead() {
        return head;
    }

    @Override
    public void setHead(Bitmap head) {
        this.head = head;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean getIs_bind_face() {
        return this.is_bind_face;
    }

    public void setIs_bind_face(boolean is_bind_face) {
        this.is_bind_face = is_bind_face;
    }
}
