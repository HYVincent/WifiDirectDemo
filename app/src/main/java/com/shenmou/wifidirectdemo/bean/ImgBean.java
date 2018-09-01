package com.shenmou.wifidirectdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.bean
 * @class describe
 * @date 2018/9/1 18:13
 */
public class ImgBean implements Parcelable {

    private String imgPath;
    private long fileLength;
    private String md5;
    private long time;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgPath);
        dest.writeLong(this.fileLength);
        dest.writeString(this.md5);
        dest.writeLong(this.time);
    }

    public ImgBean() {
    }

    protected ImgBean(Parcel in) {
        this.imgPath = in.readString();
        this.fileLength = in.readLong();
        this.md5 = in.readString();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<ImgBean> CREATOR = new Parcelable.Creator<ImgBean>() {
        @Override
        public ImgBean createFromParcel(Parcel source) {
            return new ImgBean(source);
        }

        @Override
        public ImgBean[] newArray(int size) {
            return new ImgBean[size];
        }
    };
}
