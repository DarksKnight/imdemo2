package com.gf.platform.gfplatform.widget.tooltip;

import android.content.Context;
import android.view.View;

import com.gf.platform.gfplatform.entity.Message;

/**
 * 接口
 * Created by sunhaoyang on 2016/5/3.
 */
public interface ToolTipPorts {

    void remove();

    void show(View anchor, Message message, ToolView.ControlListener listener);

    void make(Context context, ToolTipView.Builder builder);
}
