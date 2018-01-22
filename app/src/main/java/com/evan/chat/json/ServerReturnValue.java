package com.evan.chat.json;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/6
 * Time: 0:47
 */
public class ServerReturnValue {
    private boolean succeed;
    private String log;
    private Object arg1;
    private Object arg2;
    private Object[] os;

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Object getArg1() {
        return arg1;
    }

    public void setArg1(Object arg1) {
        this.arg1 = arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }

    public Object[] getOs() {
        return os;
    }

    public void setOs(Object[] os) {
        this.os = os;
    }
}
