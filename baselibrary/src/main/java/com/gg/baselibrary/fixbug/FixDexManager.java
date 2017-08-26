package com.gg.baselibrary.fixbug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;


/**
 * Created by GG on 2017/8/26.
 */

public class FixDexManager {

    private Context mContext;
    private File mDexDir;

    private String TAG = "FixDexManager";

    public FixDexManager(Context context) {
        mContext = context;
        mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复dex包
     *
     * @param fixDexPath
     */
    public void fixDex(String fixDexPath) throws Exception {

        //2获取下载好的补丁的dexElements
        //2.1 移动到系统访问的dex目录下  最终要变成ClassLoader
        File srcFile = new File(fixDexPath);

        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }

        File destFile = new File(mDexDir, srcFile.getName());

        if (destFile.exists()) {
            Log.d(TAG, "patch[" + fixDexPath + "] has be load ");
            return;
        }

        copyFile(srcFile, destFile);


        //2.2 把路径转为ClassLoader读取fix.dex 路径

        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);

    }

    /**
     * 注入到原来的applicationClassLoader中
     * @param classLoader
     * @param dexElements
     * @throws Exception
     */
    private void injectDexElements(ClassLoader classLoader, Object dexElements) throws Exception {
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);


        //2获取pathList中的dexElements

        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        dexElementsField.set(pathList,dexElements);
    }

    /**
     * 从classLoader中获取 dexElements
     *
     * @param classLoader
     * @return
     */
    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        //1先获取pathList  通过反射
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);


        //2获取pathList中的dexElements

        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }


    public void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        if (!dest.exists()) {
            dest.createNewFile();
        }
        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }


    /**
     * 合并数组
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    public Object combinArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - 1));
            }
        }
        return result;
    }

    /**
     * 加载全部的修复包
     */
    public void loadFixDex() throws Exception {
        File[]  srcFiles = mDexDir.listFiles();

        List<File> fixDexFiles = new ArrayList<>();

        for (File srcFile : srcFiles) {
            if (srcFile.getName().endsWith(".dex")){
                fixDexFiles.add(srcFile);
            }
        }

        fixDexFiles(fixDexFiles);

    }

    /**
     * 修复dex
     * @param fixDexFiles
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception {

        //1先获取已经运行的dexElements

        ClassLoader applicationClassLoader = mContext.getClassLoader();

        Object applicationDexElements = getDexElementsByClassLoader(applicationClassLoader);

       /* //2获取下载好的补丁的dexElements
        //2.1 移动到系统访问的dex目录下  最终要变成ClassLoader
        File srcFile = new File(fixDexPath);

        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }

        File destFile = new File(mDexDir, srcFile.getName());

        if (destFile.exists()) {
            Log.d(TAG, "patch[" + fixDexPath + "] has be load ");
            return;
        }

        copyFile(srcFile, destFile);


        //2.2 把路径转为ClassLoader读取fix.dex 路径

        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);*/

        File optimizedDirectory = new File(mDexDir, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();
        }


        for (File fixDexFile : fixDexFiles) {
            ClassLoader fixClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(), //dex的路径
                    optimizedDirectory,          //解压路径
                    null,                        //  .so文件的位置
                    applicationClassLoader      //父ClassLoader

            );

            Object fixDexElements = getDexElementsByClassLoader(fixClassLoader);

            //3把补丁的dexElements插入到已经运行的dexElements的最前面

            //applicationClassLoader  数组的合并

            applicationDexElements=  combinArray(fixDexElements,applicationDexElements);

        }

        //注入到原来的applicationClassLoader中

        injectDexElements(applicationClassLoader,applicationDexElements);
    }
}
