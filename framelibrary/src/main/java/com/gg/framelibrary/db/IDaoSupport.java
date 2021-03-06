package com.gg.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by GG on 2017/9/3.
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase,Class<T>clazz);

    long insert(T t);

    void insert(List<T> datas);
}
