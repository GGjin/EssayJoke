package com.gg.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.gg.framelibrary.skin.attr.SkinAttr;
import com.gg.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/9/16.
 * <p>
 * 皮肤的支持类
 */

public class SkinSupport {

    private static final String TAG = "SkinSupport";

    /**
     * 获取SkinAttr 的属性
     *
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        List<SkinAttr> skinAttrs = new ArrayList<>();


        int attrLength = attrs.getAttributeCount();
        for (int i = 0; i < attrLength; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            //只获取重要的名称 ， 值

//            Log.w(TAG, "attrName   ->" + attrName + "   ;  attrValue  ->" + attrValue);

            SkinType skinType = getSkinType(attrName);

            if (skinType != null) {

                //获取资源名称，  目前只有attrValue 是一个@int类型
                String resName = getResName(context, attrValue);

                if (TextUtils.isEmpty(resName)) {
                    continue;
                }


                SkinAttr skinAttr = new SkinAttr(resName, skinType);
                skinAttrs.add(skinAttr);
            }

        }

        return skinAttrs;
    }

    private static String getResName(Context context, String attrValue) {

        if (attrValue.startsWith("@")) {
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);

            return context.getResources().getResourceEntryName(resId);
        }

        return null;
    }

    private static SkinType getSkinType(String attrName) {

        SkinType[] skinTypes = SkinType.values();

        for (SkinType skinType : skinTypes) {
            //通过名称获取skinType
            if (skinType.getResName().equals(attrName)) {
                return skinType;
            }
        }
        return null;
    }
}
