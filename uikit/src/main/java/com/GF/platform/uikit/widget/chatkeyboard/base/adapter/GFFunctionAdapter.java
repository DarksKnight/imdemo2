package com.GF.platform.uikit.widget.chatkeyboard.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFFunction;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/3/3.
 */
public class GFFunctionAdapter extends BaseAdapter {

    private Context mContext = null;

    private List<GFFunction> mList = null;

    private Listener mListener = null;

    public GFFunctionAdapter(Context context, List<GFFunction> list, Listener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.bjmgf_message_chat_function_item, null);
            holder.ivFunction = (ImageView) convertView
                    .findViewById(R.id.bjmgf_message_chat_function_iv);
            holder.tvFunction = (TextView) convertView
                    .findViewById(R.id.bjmgf_message_chat_function_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvFunction.setText(mList.get(position).getName());
        holder.ivFunction.setImageBitmap(mList.get(position).getPic());
        if (null != mListener) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.functionSelected(mList.get(position).getName());
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {

        ImageView ivFunction;

        TextView tvFunction;
    }

    public interface Listener {
        void functionSelected(String str);
    }
}
