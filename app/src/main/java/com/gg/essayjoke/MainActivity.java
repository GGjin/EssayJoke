package com.gg.essayjoke;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gg.baselibrary.ExceptionCrashHandler;
import com.gg.baselibrary.dialog.AlertDialog;
import com.gg.baselibrary.fixbug.FixDexManager;
import com.gg.baselibrary.ioc.OnClick;
import com.gg.framelibrary.base.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import butterknife.BindView;


public class MainActivity extends BaseSkinActivity {

    private static final String TAG= "MainActivity";


    /****Hello World!****/
    @BindView(R.id.text_tv)  TextView mTextTv;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar.Builder(this)
                .setText(R.id.right_text, "返回")
                .setMiddleText("标题")
                .create();

    }

    @Override
    protected void initView() {
//        Resources resources = getSkinResources("");
//        if (resources != null) {
//            //获取文件的id
//            int drawableId = resources.getIdentifier("文件名", "drawable", "包名");
//            //根据ID获取资源对象
//            Drawable drawable = resources.getDrawable(drawableId);
//        }
    }

    /**
     * 获取其他压缩包内的资源
     *
     * @param path
     */
    private Resources getSkinResources(String path) {
        try {
            Resources resources = getResources();
            AssetManager assetManager = AssetManager.class.newInstance();

            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            //设置从哪里获取到皮肤的资源
            method.invoke(assetManager, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + path);

            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void initDate() {

        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"red.skin";

        SkinManager.getInstance().loadSkin(skinPath);

        SkinManager.getInstance().restoreDefault();
//        HttpUtils.with(this).url(ConstantValue.UrlConstant.HOME_DISCOVERY_URL)
//                .param("iid", 6152551759L).param("aid", 7)
//                .execute(new HttpCallBack<DiscoverListResult>() {
//                    @Override
//                    public void onSuccess(DiscoverListResult discoverListResult) {
//                        XLog.tag(TAG).json(new Gson().toJson(discoverListResult).toString());
//                        XLog.tag(TAG).e(discoverListResult);
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//
//                    }
//                });

//
//        IDaoSupport dao = DaoSupportFactory.getFactory().getDao(Person.class);
//        dao.insert(new Person());

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
        Log.d(TAG, "被点击了");
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.ss_comment_dialog)
                .fromBottom(true)
                .fullWidth()
                .setOnClickListener(R.id.weibo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "weibo被电击了");
                    }
                })
                .show();
    }
}
