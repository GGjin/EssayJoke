package com.gg.essayjoke;

import android.os.Environment;
import android.util.Log;

import com.gg.baselibrary.fixbug.FixDexManager;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.ExceptionCrashHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class MainActivity extends BaseSkinActivity {


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initDate() {

        File file = ExceptionCrashHandler.getInstance().getCrashFile();
        if (file.exists()) {
            //上传到服务器

            try {
                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(file));

                char[] buffer = new char[1024];
                int len = 0;
                while ((len = fileReader.read(buffer)) != 1) {
                    String str = new String(buffer, 0, len);
                    Log.d("TAG", str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        fixDexBug();
    }

    private void fixDexBug() {

        //先联网下载修复包

        //获取修复包
        File file = new File(Environment.getExternalStorageDirectory(), "fix.dex");

        if (file.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
