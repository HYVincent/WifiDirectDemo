package com.shenmou.wifidirectdemo.bean;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.bean
 * @class describe
 * @date 2018/8/24 11:35
 */
public class MsgBean {

    private String msg;
    //0 一般消息  1 错误消息  2 警告消息
    private int msgLevel;
    private long time;

    public int getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(int msgLevel) {
        this.msgLevel = msgLevel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
