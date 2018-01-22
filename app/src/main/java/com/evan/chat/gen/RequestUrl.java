package com.evan.chat.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA
 * User: yejiaquan
 * Date: 2018/1/22
 * Time: 下午3:19
 */

@Entity
public class RequestUrl {
    @Id(autoincrement = true)
    private Long id;

    private String key;

    private String value;

    @Generated(hash = 396516149)
    public RequestUrl(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    @Generated(hash = 1622871348)
    public RequestUrl() {
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
