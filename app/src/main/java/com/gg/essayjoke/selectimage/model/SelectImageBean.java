package com.gg.essayjoke.selectimage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator :  GG
 * Time    :  2017/9/27
 * Mail    :  gg.jin.yu@gmail.com
 * Explain :
 */

public class SelectImageBean implements Parcelable {

    private String name ;

    private String path ;

    private long time ;

    public SelectImageBean(String name, String path, long time) {
        this.name = name;
        this.path = path;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectImageBean)) return false;

        SelectImageBean that = (SelectImageBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return path != null ? path.equals(that.path) : that.path == null;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.time);
    }

    protected SelectImageBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<SelectImageBean> CREATOR = new Parcelable.Creator<SelectImageBean>() {
        @Override
        public SelectImageBean createFromParcel(Parcel source) {
            return new SelectImageBean(source);
        }

        @Override
        public SelectImageBean[] newArray(int size) {
            return new SelectImageBean[size];
        }
    };
}
