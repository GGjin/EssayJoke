package com.gg.baselibrary.network;

/**
 * Created by GG on 2017/8/30.
 */

public abstract class HttpCallBack<T> {

    public abstract void onSuccess(T request);

    public abstract void onFailure(Exception e);

}
