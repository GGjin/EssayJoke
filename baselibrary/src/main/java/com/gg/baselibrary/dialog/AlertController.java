package com.gg.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * Created by GG on 2017/8/27.
 */

class AlertController {

    private AlertDialog mAlertDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog dialog, Window window) {
        mAlertDialog = dialog;
        mWindow = window;
    }


    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        mViewHelper = viewHelper;
    }

    /**
     * 设置文本的显示
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param weakReference
     */
    public void setOnClickListener(int viewId, WeakReference<View.OnClickListener> weakReference) {
        mViewHelper.setOnClickListener(viewId, weakReference);
    }


    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }


    public static class AlertParams {

        public Context mContext;
        //主题的id
        public int mThemeResId;
        //点击空白是否能够取消
        public boolean mCancelable = true;
        //dialog的取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog的消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog的按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //显示的布局
        public View mView;
        //布局的id
        public int mViewLayoutResId = 0;
        //存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //存放点击事件
        public SparseArray<WeakReference<View.OnClickListener>> mClickArray = new SparseArray<>();
        //设置宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //添加动画
        public int mAnimation = 0;
        //位置
        public int mGravity = Gravity.CENTER;
        //设置高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {
            mContext = context;
            mThemeResId = themeResId;
        }

        public void apply(AlertController alert) {


            DialogViewHelper viewHelper = null;
//            1.设置布局
            if (mThemeResId != 0)
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);

            if (mView != null)
                viewHelper = new DialogViewHelper(mView);

            if (viewHelper == null)
                throw new IllegalArgumentException("请设置setContentView方法 传入布局或布局id");

            alert.getAlertDialog().setContentView(viewHelper.getContentView());

            //设置Controller 的辅助类
            alert.setViewHelper(viewHelper);

//            2.设置文本

            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }


//            3.设置点击事件
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }


//            4.设置自定义的一些效果   动画 从底部弹出  默认动画

            Window window = alert.getWindow();

            window.setGravity(mGravity);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;

            window.setAttributes(params);

            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }


        }


    }
}
