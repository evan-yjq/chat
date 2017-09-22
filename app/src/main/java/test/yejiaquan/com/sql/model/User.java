package test.yejiaquan.com.sql.model;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * 用户名建议用真名
     * 用户名为ID外标志符
     * 不得重复
     */

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 个人简介
     */
    private String signature;

    /**
     * 头像
     */
    private String state;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
