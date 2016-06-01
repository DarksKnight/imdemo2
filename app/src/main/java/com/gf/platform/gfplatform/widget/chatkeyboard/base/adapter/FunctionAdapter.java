package com.gf.platform.gfplatform.widget.chatkeyboard.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.platform.gfplatform.R;
import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.Function;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/3/3.
 */
public class FunctionAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Function> mList = null;

    public FunctionAdapter (Context context, List<Function> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bjmgf_message_chat_function_item, null);
            holder.ivFunction = (ImageView)convertView.findViewById(R.id.bjmgf_message_chat_function_iv);
            holder.tvFunction = (TextView)convertView.findViewById(R.id.bjmgf_message_chat_function_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvFunction.setText(mList.get(position).getName());
        holder.ivFunction.setImageBitmap(mList.get(position).getPic());
        return convertView;
    }

    static class ViewHolder {
        ImageView ivFunction;
        TextView tvFunction;
    }
}
