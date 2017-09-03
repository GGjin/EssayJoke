package com.gg.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;

/**
 * Created by GG on 2017/9/3.
 */

public class DapSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClass;

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        mSQLiteDatabase = sqLiteDatabase;
        mClass = clazz;


        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("create table if not exists ").append(DaoUtils.getTableName(mClass)).append("(id integer primary key " +
                "autoincrement, ");

        Field[] fields = mClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            stringBuilder.append(name).append(DaoUtils.getComlumnType(type)).append(" ,");
        }

        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ")");

        String createSq = stringBuilder.toString();

        mSQLiteDatabase.execSQL(createSq);

    }


    @Override
    public int insert(Object o) {


        return 0;
    }

}
