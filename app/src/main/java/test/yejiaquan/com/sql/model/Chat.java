package test.yejiaquan.com.sql.model;

import java.io.Serializable;

/**
 * Created by yejiaquan on 16/7/12.
 */
public class Chat implements Serializable {
    private String time;
    private String user;
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
