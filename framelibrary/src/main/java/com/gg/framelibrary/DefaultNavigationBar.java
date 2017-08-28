package com.gg.framelibrary;

import android.content.Context;
import android.view.ViewGroup;

import com.gg.baselibrary.navigationbar.AbsNavigationBar;

/**
 * Created by GG on 2017/8/29.
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.DefaultNavigationBuilder.DefaultNavigationParams> {

    private DefaultNavigationBar(DefaultNavigationBuilder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return 0;
    }

    @Override
    public void applyViews() {

    }

    public class DefaultNavigationBuilder extends Builder {

        DefaultNavigationParams P ;

        public DefaultNavigationBuilder(Context context, ViewGroup parent) {
            super(context, parent);
            P=new DefaultNavigationParams(context,parent);
        }

        public DefaultNavigationBuilder(Context context) {
            super(context);
            P= new DefaultNavigationParams(context,null);
        }

        @Override
        public DefaultNavigationBar create() {
            return new DefaultNavigationBar(P);
        }

        public class DefaultNavigationParams extends AbsNavigationBar.Builder.AbsNavigationParams {

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
