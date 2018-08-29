package com.shenmou.wifidirectdemo.bean;

import java.io.Serializable;

/**
 * date：2018/2/24 on 17:00
 * description: 文件详情
 */

public class FileBean implements Serializable{

    public static final String serialVersionUID = "6321689524634663223356";
    private String data;
    private String filePath;

    private long fileLength;

    //MD5码：保证文件的完整性
    private String md5;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
