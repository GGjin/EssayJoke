package com.gg.essayjoke.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GG on 2017/9/3.
 */

public class Person {
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
