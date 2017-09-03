package com.gg.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by GG on 2017/9/3.
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase,Class<T>clazz);

    int insert(T t);
}
