package com.gf.platform.gfplatform.base;

/**
 * Created by apple on 1/25/15.
 */
public class BaseRule extends CommonRule<BaseEntity> {

    //单例模式
    private static class SingletonHolder {
        private static final BaseRule INSTANCE = new BaseRule();
    }

    private DealProxy.DealObserve observe = null;

    public static final BaseRule getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public CommonRule dealEntity(BaseEntity entity) {
        if(!entity.getCode().equals("0")){
            observe.onFail(entity.getErrMeg());
        }else{
            observe.onSuccess(entity);
        }
        return super.dealEntity(entity);
    }

    @Override
    public CommonRule setDealObserve(DealProxy.DealObserve observe) {
        this.observe=observe;
        return super.setDealObserve(observe);
    }
}
