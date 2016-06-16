package com.GF.platform.uikit.widget.chatkeyboard.base.ports;

import android.view.View;
import android.view.ViewGroup;

import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageEntity;


/**
 * Created by sunhaoyang on 2016/4/19.
 */
public interface GFPageViewInstantiateListener<T extends GFPageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
