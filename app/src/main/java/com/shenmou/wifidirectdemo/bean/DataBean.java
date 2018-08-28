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
    private String base64;
    private byte[] img;
    private Object data;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

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
