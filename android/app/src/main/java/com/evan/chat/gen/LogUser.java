package com.evan.chat.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LogUser {

    @Id(autoincrement = true)
    private Long id;
    private int user_id;
    private String password;
    private Date login_time;
    private int is_auto;


    @Generated(hash = 1478805517)
    public LogUser(Long id, int user_id, String password, Date login_time,
            int is_auto) {
        this.id = id;
        this.user_id = user_id;
        this.password = password;
        this.login_time = login_time;
        this.is_auto = is_auto;
    }

    @Generated(hash = 2145889911)
    public LogUser() {
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

    public int getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(int is_auto) {
        this.is_auto = is_auto;
    }
}
