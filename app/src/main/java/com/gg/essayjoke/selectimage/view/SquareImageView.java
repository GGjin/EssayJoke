package com.gg.essayjoke.selectimage.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Creator :  GG
 * Time    :  2017/9/27
 * Mail    :  gg.jin.yu@gmail.com
 * Explain :
 */

public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

//        // get width
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        // set height = width
//        int height = width;
//
//        setMeasuredDimension(width, height);

    }
}
