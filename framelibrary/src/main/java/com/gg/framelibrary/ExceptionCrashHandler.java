package com.gg.framelibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.currentThread;

/**
 * Created by GG on 2017/8/25.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {


    private static ExceptionCrashHandler mInstance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;


    private ExceptionCrashHandler() {

    }

    public static ExceptionCrashHandler getInstance() {
        if (mInstance != null) {
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance != null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        currentThread().setUncaughtExceptionHandler(this);
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {


        String str = saveInfoToSD(throwable);

        cacheCrashFile(str);
    }


    /**
     * 保存崩溃信息到SD中
     *
     * @param throwable
     */
    private String saveInfoToSD(Throwable throwable) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        //1 手机信息和应用信息
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("/n");
        }

        //2 崩溃的详细信息
        sb.append(obtaionExceptionInfo(throwable));

        //3.保存文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(mContext.getFilesDir(), File.separator + "crash" + File.separator);

            //先删除之前的异常信息
            if (dir.exists()) {
                //删除目录下的所有子文件
                deleteDir(dir);
            }

            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return fileName;

    }


    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();

        PackageManager mPackagManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackagManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", mPackageInfo.versionCode + "");
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());

        return map;
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            //循环删除文件
            for (File file : files) {
                file.delete();
            }
        }
        return true;
    }

    /**
     * 获取系统未捕捉的异常信息
     *
     * @param throwable
     * @return
     */
    private String obtaionExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }


    /**
     * 获取手机的信息
     *
     * @return
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            //利用反射获取Build的所有属性
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name).append("=").append(value).append("/n");

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getAssignTime(String dateFormatstr) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatstr);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }


    /**
     * 缓存崩溃日志文件
     *
     * @param fileName
     */
    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", fileName).apply();
    }

    /**
     * 获取缓存文件名称
     *
     * @return
     */
    public File getCrashFile() {
        String fileName = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE).getString("CRASH_FILE_NAME", "");
        return new File(fileName);
    }

}
