package com.GF.platform.uikit.widget.tooltip;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.GF.platform.uikit.entity.GFMessage;


/**
 * 消息长按复制删除工具
 * Created by sunhaoyang on 2016/5/3.
 */
public class GFToolTipView implements GFToolTipPorts {

    private GFToolView view = null;
    private ListView listView = null;
    private ViewInstall viewInstall = null;
    private static GFToolTipView single = null;

    //单例
    public static synchronized GFToolTipView getInstance() {
        if (single == null) {
            single = new GFToolTipView();
        }
        return single;
    }

    @Override
    public void remove() {
        if (null != viewInstall) {
            viewInstall.doRemove(view);
        }
    }

    @Override
    public void show(View anchor, GFMessage gFMessage, GFToolView.ControlListener listener, ViewInstall install) {
        viewInstall = install;
        if (null != viewInstall) {
            viewInstall.doShow(view, listView, anchor, gFMessage, listener);
        }
    }

    @Override
    public void make(Builder builder) {
        this.view = builder.view;
        this.listView = builder.listView;
    }

    public static final class Builder {
        public GFToolView view = null;
        public View v = null;
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

        public Builder setView(View v) {
            this.v = v;
            return this;
        }

        public Builder build() {
            return this;
        }
    }

    public interface ViewInstall<T> {
        void doShow(T view, ListView listView, View anchor, Object... objs);
        void doRemove(View view);
    }
}
