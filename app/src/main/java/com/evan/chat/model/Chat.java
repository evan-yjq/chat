package com.evan.chat.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Chat{

    @Id(autoincrement = true)
    private Long chat_id;
    private String send_time;
    private int from_user_id;
    private int to_user_id;
    private String content;

    @Generated(hash = 214656087)
    public Chat(Long chat_id, String send_time, int from_user_id, int to_user_id,
            String content) {
        this.chat_id = chat_id;
        this.send_time = send_time;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.content = content;
    }

    @Generated(hash = 519536279)
    public Chat() {
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
