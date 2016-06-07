package com.GF.platform.uikit.widget.tooltip;

import android.content.Context;
import android.view.View;

/**
 * 接口
 * Created by sunhaoyang on 2016/5/3.
 */
public interface ToolTipPorts {

    void remove();

    void show(View anchor, int msgIndex, String msgContent, int type, ToolView.ControlListener listener);

    void make(Context context, ToolTipView.Builder builder);
}
