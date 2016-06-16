package com.GF.platform.uikit.widget.tooltip;

import android.content.Context;
import android.view.View;

import com.GF.platform.uikit.entity.GFMessage;

/**
 * 接口
 * Created by sunhaoyang on 2016/5/3.
 */
public interface GFToolTipPorts {

    void remove();

    void show(View anchor, GFMessage GFMessage, GFToolView.ControlListener listener);

    void make(Context context, GFToolTipView.Builder builder);
}
