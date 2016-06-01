package com.gf.platform.gfplatform.base;

import java.util.List;

/**
 * Created by apple on 1/25/15.
 */
public abstract class CommonRule<T> {

    private DealProxy.DealObserve observe;
    private DealProxy.DealObserveList observeList;

    public CommonRule dealEntity(T entity){
        return this;
    }

    public CommonRule dealList (List<T> listData) {
        return this;
    }

    public CommonRule setDealObserve(DealProxy.DealObserve observe){
        this.observe=observe;
        return this;
    }

    public CommonRule setDealObserveList (DealProxy.DealObserveList observeList) {
        this.observeList = observeList;
        return  this;
    }
}
