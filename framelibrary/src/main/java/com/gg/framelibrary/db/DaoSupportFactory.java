package com.gg.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by GG on 2017/9/3.
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;

    private SQLiteDatabase mSQLiteDatabase;

    private DaoSupportFactory() {


        //判断是否有权限。。 6.0以上需要动态申请
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "nhdz" + File
                .separator + "database");
        if (!dbRoot.exists())
            dbRoot.mkdirs();

        File dbFile = new File(dbRoot, "nhdz.db");

        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactory() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null)
                    mFactory = new DaoSupportFactory();
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport getDao(Class<T> clazz) {
        IDaoSupport<T> dao = new DapSupport<>();
        dao.init(mSQLiteDatabase, clazz);
        return dao;
    }
}
