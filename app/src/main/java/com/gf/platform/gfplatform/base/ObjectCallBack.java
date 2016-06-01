package com.gf.platform.gfplatform.base;

/**
 * Created by apple on 1/24/15.
 */
public interface ObjectCallBack<T> {
    void getData(T obj);
    void getError(String error);
}
