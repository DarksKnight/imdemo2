package com.gf.platform.gfplatform.widget.chatkeyboard.base.ports;

import android.view.View;
import android.view.ViewGroup;

import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.PageEntity;

/**
 * Created by sunhaoyang on 2016/4/19.
 */
public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
