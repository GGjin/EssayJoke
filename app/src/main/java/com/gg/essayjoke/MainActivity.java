package com.gg.essayjoke;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gg.baselibrary.dialog.AlertDialog;
import com.gg.baselibrary.fixbug.FixDexManager;
import com.gg.baselibrary.ioc.OnClick;
import com.gg.baselibrary.ioc.ViewBind;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.baselibrary.ExceptionCrashHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class MainActivity extends BaseSkinActivity {

    private String TAG = "MainActivity";


    /****Hello World!****/
    @ViewBind(R.id.text_tv) private TextView mTextTv;

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



//        fixDexBug();
    }

    private void fixDexBug() {

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


        //先联网下载修复包

        //获取修复包
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");

        if (fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.text_tv)
    private void textTvClick(TextView textTv) {
        Log.d(TAG,"被点击了");
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.ss_comment_dialog)
                .fromBottom(true)
                .fullWidth()
                .setOnClickListener(R.id.weibo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"weibo被电击了");
                    }
                })
                .show();
    }
}
