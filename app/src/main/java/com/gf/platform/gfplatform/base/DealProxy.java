package com.GF.platform.gfplatform.base;

import java.util.List;

/**
 * Created by apple on 1/25/15.
 */
public class DealProxy<T> {

    private static ObjectCallBack obj;
    private static ListCallBack listObj;

    //单例模式
    private static class SingletonHolder {
        private static final DealProxy INSTANCE = new DealProxy();
    }

    public static final DealProxy getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 关联model（有规则）
     *
     * @param entity
     * @param rule
     */
    public synchronized void dealModel(BaseEntity entity, CommonRule rule) {
        dealData(entity, rule);
    }

    public synchronized void dealModel (List<T> listData, CommonRule rule) {
        dealData(listData, rule);
    }

    /**
     * 关联model（无规则）
     *
     * @param entity
     */
    public synchronized void dealModel(BaseEntity entity) {
        dealData(entity);
    }

    public synchronized void dealModel(List<T> listData) {
        dealData(listData);
    }

    /**
     * 关联view
     *
     * @param obj
     * @param model
     */
    public synchronized void dealView(ObjectCallBack<T> obj, CommonModel model) {
        DealProxy.obj = obj;
        model.getData();
    }

    public synchronized void dealView (ListCallBack<T> listObj, CommonModel model) {
        DealProxy.listObj = listObj;
        model.getData();
    }

    /**
     * 处理数据（有规则）
     *
     * @param entity
     * @param rule
     */
    synchronized void dealData(BaseEntity entity, CommonRule rule) {
        rule.setDealObserve(new DealObserve() {
            @Override
            public void onSuccess(BaseEntity entity) {
                obj.getData(entity);
            }

            @Override
            public void onFail(String error) {
                obj.getError(error);
            }
        }).dealEntity(entity);
    }

    synchronized void dealData(List<T> listData, CommonRule rule) {
        rule.setDealObserveList(new DealObserveList<T>() {
            @Override
            public void onSuccess(List<T> listData) {
                listObj.getData(listData);
            }

            @Override
            public void onFail(String error) {
                obj.getError(error);
            }
        }).dealList(listData);
    }

    /**
     * 处理数据（无规则，有风险，如果数据错误则会出现数据错乱）
     *
     * @param entity
     */
    synchronized void dealData(BaseEntity entity) {
        obj.getData(entity);
    }

    synchronized void dealData (List<T> listData) {
        listObj.getData(listData);
    }

    /**
     * 处理数据（无特殊规则，使用默认规则）
     *
     * @param entity
     */
    synchronized void dealDefaultData(BaseEntity entity) {
        BaseRule.getInstance().setDealObserve(new DealObserve() {
            @Override
            public void onSuccess(BaseEntity entity) {
                obj.getData(entity);
            }

            @Override
            public void onFail(String error) {
                obj.getError(error);
            }
        }).dealEntity(entity);
    }

    /**
     * 关联接口，判断成功和失败
     */
    public interface DealObserve {
        void onSuccess(BaseEntity entity);

        void onFail(String error);
    }

    public interface DealObserveList<T> {
        void onSuccess(List<T> listData);

        void onFail(String error);
    }
}