package com.GF.platform.uikit.widget.tooltip;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.R;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.util.GFUtil;


/**
 * 消息长按复制删除工具
 * Created by sunhaoyang on 2016/5/3.
 */
public class GFToolTipView implements GFToolTipPorts {

    private GFToolView view = null;
    private Context context = null;
    private static GFToolTipView single = null;
    private ViewGroup rootView = null;
    private ListView listView = null;
    private int toolViewWidth = 0;
    private int toolViewHeight = 0;

    //单例
    public static synchronized GFToolTipView getInstance() {
        if (single == null) {
            single = new GFToolTipView();
        }
        return single;
    }

    @Override
    public void remove() {
        if (null != rootView) {
            rootView.removeView(view);
        }
    }

    @Override
    public void show(View anchor, GFMessage GFMessage, GFToolView.ControlListener listener) {
        rootView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        toolViewWidth = (int)context.getResources().getDimension(R.dimen.gf_205dp);
        toolViewHeight = (int)context.getResources().getDimension(R.dimen.gf_40dp);
        rootView.removeView(view);
        rootView.addView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        int[] listViewLocation = new int[2];
        listView.getLocationOnScreen(listViewLocation);
        int listViewY = listViewLocation[1];
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        int width = anchor.getMeasuredWidth() - toolViewWidth;
        int leftMargin = x + width/2;
        int topMargin = 0;

        if ((y - listViewY) < toolViewHeight) {
            view.setDirection(GFToolView.Direction.UP);
            topMargin = y + anchor.getMeasuredHeight();
        } else {
            view.setDirection(GFToolView.Direction.DOWN);
            topMargin = y - toolViewHeight;
        }

        int screenWidth = GFUtil.getScreenWidth(context);
        int distance = toolViewWidth - screenWidth / 2;

        if (leftMargin > 0 && leftMargin + distance > screenWidth / 2) {
            if (screenWidth / 2 < toolViewWidth) {
                leftMargin -= distance;
                view.setArrowLocation(distance , 0);
            }
        } else if (leftMargin < 0) {
            leftMargin += distance;
            view.setArrowLocation(0 ,distance);
        }

        params.setMargins(leftMargin, topMargin, 0, 0);
        view.setLayoutParams(params);

        view.setGFMessage(GFMessage);
        if (GFMessage.getType() == GFConstant.MSG_TYPE_EXPRESSION) {
            view.setType(GFToolView.Type.EMOTICON);
        } else if (GFMessage.getType() == GFConstant.MSG_TYPE_TEXT) {
            view.setType(GFToolView.Type.TEXT);
        } else if (GFMessage.getType() == GFConstant.MSG_TYPE_AUDIO) {
            view.setType(GFToolView.Type.VOICE);
        }
        view.setListener(listener);
    }

    @Override
    public void make(Context context, Builder builder) {
        this.view = builder.view;
        this.listView = builder.listView;
        this.context = context;
    }

    public static final class Builder {
        public GFToolView view = null;
        public Context context = null;
        public ListView listView = null;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setListView(ListView listView) {
            this.listView = listView;
            return this;
        }

        public Builder setToolTipView(GFToolView view) {
            this.view = view;
            return this;
        }

        public Builder build() {
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return this;
        }
    }
}
