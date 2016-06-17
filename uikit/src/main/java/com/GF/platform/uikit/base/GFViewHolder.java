package com.GF.platform.uikit.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunhaoyang on 2016/3/23.
 */
public class GFViewHolder {
    private SparseArray<View> views;

    private View convertView;

    private GFViewHolder(Context context, ViewGroup parent, int resId,
                         int position) {
        this.views = new SparseArray<View>();
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resId, null);
        convertView.setTag(this);
    }

    public static GFViewHolder get(Context context, View convertView,
                                   ViewGroup parent, int resId, int position) {
        if (convertView == null) {
            return new GFViewHolder(context, parent, resId, position);
        }
        return (GFViewHolder) convertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }
}
