package com.gg.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by GG on 2017/9/3.
 */

public class DapSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClass;

    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String, Method> mPutMethods = new ArrayMap<>();

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
            stringBuilder.append(name).append(DaoUtils.getColumnType(type)).append(" ,");
        }

        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ")");

        String createSq = stringBuilder.toString();

        mSQLiteDatabase.execSQL(createSq);

    }

    @Override
    public void insert(List<T> datas) {
        mSQLiteDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }

        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }


    @Override
    public long insert(T o) {
        ContentValues contentValues = contentValueByObj(o);

        return mSQLiteDatabase.insert(DaoUtils.getTableName(mClass), null, contentValues);
    }

    private ContentValues contentValueByObj(T t) {
        ContentValues values = new ContentValues();

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);

                String key = field.getName();
                Object value = field.get(t);
                if (value==null)
                    continue;

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                String fieldTypeName = field.getType().getName();
                Method putMethod = mPutMethods.get(fieldTypeName);

                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());

                    mPutMethods.put(fieldTypeName, putMethod);
                }
                putMethod.invoke(values, key, value);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }

        }


        return values;
    }

}
