package com.gf.platform.gfplatform.ui.activity.message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gf.platform.gfplatform.R;
import com.gf.platform.gfplatform.entity.Message;
import com.gf.platform.gfplatform.ui.activity.message.MessageActivity;
import com.gf.platform.gfplatform.widget.circleimageview.CircleImageView;
import com.gf.platform.gfplatform.widget.emojitextview.EmojiTextView;
import com.gf.platform.gfplatform.widget.tooltip.ToolTipView;
import com.gf.platform.gfplatform.widget.tooltip.ToolView;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/3/2.
 */
public class MessageListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Message> mList = null;
    private ToolView.ControlListener listener = null;

    public MessageListAdapter(Context context, List<Message> list, ToolView.ControlListener listener) {
        mContext = context;
        mList = list;
        this.listener = listener;
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
    public int getItemViewType(int position) {
        if (mList.get(position).getCategory() == Message.Category.NORMAL_ME) {
            return 1;
        } else if (mList.get(position).getCategory() == Message.Category.NORMAL_YOU) {
            return 0;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Message msg = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if (msg.getCategory() == Message.Category.NORMAL_ME || msg.getCategory() == Message.Category.NORMAL_YOU) {
                if (msg.getCategory() == Message.Category.NORMAL_ME) {
                    convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_me_item, null);
                } else if (msg.getCategory() == Message.Category.NORMAL_YOU) {
                    convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_you_item, null);
                }
                holder.tvChat = (EmojiTextView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_tv);
                holder.tvTime = (TextView) convertView.findViewById(R.id.bjmgf_message_chat_time_tv);
                holder.ivFace = (CircleImageView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_face_iv);
                holder.ivChat = (ImageView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_expression);
                holder.rl = (RelativeLayout) convertView.findViewById(R.id.bjmgf_message_chat_root);
                holder.ivSelect = (ImageView) convertView.findViewById(R.id.bjmgf_message_chat_list_select);
            } else if (msg.getCategory() == Message.Category.NURTURE) {

            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (msg.getCategory() == Message.Category.NORMAL_ME || msg.getCategory() == Message.Category.NORMAL_YOU) {
            normalChat(msg, convertView, holder);
        } else if (msg.getCategory() == Message.Category.NURTURE) {
            nurtureChat(convertView);
        }

        return convertView;
    }

    /**
     * 求包养对话框
     * @param convertView
     */
    private void nurtureChat(View convertView) {

    }

    /**
     * 普通对话框
     * @param msg
     * @param convertView
     * @param holder
     */
    private void normalChat(Message msg, View convertView, ViewHolder holder) {
        if (msg.isShowSelected()) {
            holder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelect.setVisibility(View.GONE);
        }

        if (msg.isChecked()) {
            holder.ivSelect.setBackgroundResource(R.mipmap.bjmgf_message_chat_list_del_checked);
        } else {
            holder.ivSelect.setBackgroundResource(R.mipmap.bjmgf_message_chat_list_del_normal);
        }

        if (msg.getExpression() != null) {
            holder.ivChat.setImageBitmap(msg.getExpression());
            holder.tvChat.setVisibility(View.GONE);
            holder.ivChat.setVisibility(View.VISIBLE);
        } else {
            holder.tvChat.setText(msg.getInfo());
            holder.ivChat.setVisibility(View.GONE);
            holder.tvChat.setVisibility(View.VISIBLE);
        }
        holder.tvTime.setText(msg.getDate());

        holder.tvChat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (((MessageActivity) mContext).currentStatus == MessageActivity.Status.NORMAL) {
                    ToolTipView.getInstance().show(v, msg, listener);
                }
                return true;
            }
        });

        holder.ivChat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (((MessageActivity) mContext).currentStatus == MessageActivity.Status.NORMAL) {
                    ToolTipView.getInstance().show(v, msg, listener);
                }
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipView.getInstance().remove();
                if (((MessageActivity) mContext).currentStatus == MessageActivity.Status.EDIT) {
                    if (msg.isChecked()) {
                        msg.setChecked(false);
                        holder.ivSelect.setBackgroundResource(R.mipmap.bjmgf_message_chat_list_del_normal);
                    } else {
                        msg.setChecked(true);
                        holder.ivSelect.setBackgroundResource(R.mipmap.bjmgf_message_chat_list_del_checked);
                    }
                }
            }
        });
    }

    public static class ViewHolder {
        //对话（包含你和我）
        TextView tvTime;
        EmojiTextView tvChat;
        ImageView ivChat;
        CircleImageView ivFace;
        RelativeLayout rl;
        ImageView ivSelect;


    }
}
