package com.shenmou.wifidirectdemo.bean;

import java.io.Serializable;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.bean
 * @class describe
 * @date 2018/8/27 16:18
 */
public class DataBean implements Serializable{
    // 1 文本  2 图片
    private int dataType;

    private Object data;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "dataType=" + dataType +
                ", data=" + data +
                '}';
    }
}
