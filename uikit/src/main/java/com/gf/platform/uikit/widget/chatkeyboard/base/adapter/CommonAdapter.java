package com.GF.platform.uikit.widget.chatkeyboard.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * Created by sunhaoyang on 2016/3/23.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    private List<T> data;
    private int layoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        super();
        this.context = context;
        this.data = data;
        this.layoutId  = layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder baseViewHolder = ViewHolder.get(context, convertView, parent, layoutId, position);
        convert(baseViewHolder,data.get(position));
        return baseViewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, T t) ;
}
