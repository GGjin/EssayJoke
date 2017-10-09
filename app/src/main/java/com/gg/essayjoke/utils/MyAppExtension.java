package com.gg.essayjoke.utils;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * Creator : GG
 * Time    : 2017/9/29
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

@GlideExtension
public class MyAppExtension {

    //Size for mini thumb pixels
    private static final int MINI_THUMB_SIZE = 100;

    private MyAppExtension() {
    }

    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options
                .fitCenter()
                .override(MINI_THUMB_SIZE);
    }


}
