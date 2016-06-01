package com.gf.platform.gfplatform.base;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/3/30.
 */
public interface ListCallBack<T> {
    void getData(List<T> listData);
    void getError(String error);
}
