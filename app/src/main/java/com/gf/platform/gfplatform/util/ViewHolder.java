package com.gf.platform.gfplatform.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunhaoyang on 2016/3/23.
 */
public class ViewHolder {
    private SparseArray<View> views;

    private View convertView;

    private ViewHolder(Context context, ViewGroup parent, int resId,
                       int position) {
        this.views = new SparseArray<View>();
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resId, null);
        convertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int resId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, resId, position);
        }
        return (ViewHolder) convertView.getTag();
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
