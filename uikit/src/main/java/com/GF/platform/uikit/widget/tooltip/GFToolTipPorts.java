package com.GF.platform.uikit.widget.tooltip;

import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.widget.tooltip.GFToolTipView;
import com.GF.platform.uikit.widget.tooltip.GFToolView;

import android.view.View;

/**
 * 接口
 * Created by sunhaoyang on 2016/5/3.
 */
public interface GFToolTipPorts {

    void remove();

    void show(View anchor, GFMessage GFMessage, GFToolView.ControlListener listener, GFToolTipView.ViewInstall install);

    void make(GFToolTipView.Builder builder);
}
