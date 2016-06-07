package com.GF.platform.gfplatform.model.message.impl;

import com.GF.platform.gfplatform.base.Global;
import com.GF.platform.gfplatform.entity.Message;
import com.GF.platform.gfplatform.model.message.MessageFragementModel;

/**
 * Created by sunhaoyang on 2016/3/30.
 */
public class MessageFragmentModelImpl implements MessageFragementModel {

    @Override
    public void getData() {
        Global.MESSAGES.clear();
        for (int i = 0; i < 20; i++) {
            Message m = new Message();
            m.setDate("星期三");
            m.setInfo("[惬意]");
            m.setNickName("火星来客" + i);
            m.setOldPosition(i);
            Global.MESSAGES.add(m);
        }
        dealProxy.dealModel(Global.MESSAGES);
    }
}
