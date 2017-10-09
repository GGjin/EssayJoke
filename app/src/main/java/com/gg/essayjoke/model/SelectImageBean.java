package com.gg.essayjoke.model;

/**
 * Creator :  GG
 * Time    :  2017/9/27
 * Mail    :  gg.jin.yu@gmail.com
 * Explain :
 */

public class SelectImageBean {

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

}
