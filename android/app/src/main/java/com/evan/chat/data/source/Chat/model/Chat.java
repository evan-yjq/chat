package com.evan.chat.data.source.Chat.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Chat{

    @Id(autoincrement = true)
    private Long chat_id;
    private long send_time;
    private long from_user_id;
    private long to_user_id;
    private int sender;
    private String content;

    @Generated(hash = 2093090517)
    public Chat(Long chat_id, long send_time, long from_user_id, long to_user_id,
            int sender, String content) {
        this.chat_id = chat_id;
        this.send_time = send_time;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.sender = sender;
        this.content = content;
    }

    @Generated(hash = 519536279)
    public Chat() {
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public long getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(long from_user_id) {
        this.from_user_id = from_user_id;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
