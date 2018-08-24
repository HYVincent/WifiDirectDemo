package com.common.util.bean;

import java.io.Serializable;

/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name StarKangMedical
 * @page com.vincent.mylibrary.entity
 * @class describe
 * @date 2017/12/12 10:55
 */

public class EventMsg<T> implements Serializable {

    private String msgType;

    private T msgContent;

    private int position;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public T getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(T msgContent) {
        this.msgContent = msgContent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}