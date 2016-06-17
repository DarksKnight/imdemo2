package com.GF.platform.uikit.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhaoyang on 2016/3/23.
 */
public abstract class GFCommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> data = new ArrayList<>();
    protected int layoutId;

    public GFCommonAdapter(Context context, List<T> data, int layoutId) {
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
        GFViewHolder baseGFViewHolder = GFViewHolder.get(context, convertView, parent, layoutId, position);
        convert(baseGFViewHolder,data.get(position), position);
        return baseGFViewHolder.getConvertView();
    }

    public abstract void convert(GFViewHolder GFViewHolder, T t, int position) ;
}
