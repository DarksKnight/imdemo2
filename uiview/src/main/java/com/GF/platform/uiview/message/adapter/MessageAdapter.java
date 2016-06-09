package com.GF.platform.uiview.message.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.GF.platform.uikit.widget.circleimageview.CircleImageView;
import com.GF.platform.uikit.widget.customimage.CustomRlImage;
import com.GF.platform.uikit.widget.emojitextview.EmojiTextView;
import com.GF.platform.uikit.widget.tooltip.ToolTipView;
import com.GF.platform.uikit.widget.tooltip.ToolView;
import com.GF.platform.uiview.R;
import com.GF.platform.uikit.entity.Message;
import com.GF.platform.uiview.message.MessageView;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/6/8.
 */

public class MessageAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Message> mList = null;
    private ToolView.ControlListener listener = null;
    private MessageView mView = null;

    public MessageAdapter(Context context, List<Message> list, ToolView.ControlListener listener, MessageView view) {
        mContext = context;
        mList = list;
        this.listener = listener;
        mView = view;
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
        if (mList.get(position).getCategory() == Message.Category.NURTURE){
            return 2;
        } else if (mList.get(position).getCategory() == Message.Category.NORMAL_ME) {
            return 1;
        } else if (mList.get(position).getCategory() == Message.Category.NORMAL_YOU) {
            return 0;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
                convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_nurture_item, null);
                holder.rlImage = (CustomRlImage) convertView.findViewById(R.id.bjmgf_message_chat_nurture_bg);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (msg.getCategory() == Message.Category.NORMAL_ME || msg.getCategory() == Message.Category.NORMAL_YOU) {
            normalChat(position, msg, convertView, holder);
        } else if (msg.getCategory() == Message.Category.NURTURE) {
            nurtureChat(holder);
        }

        return convertView;
    }

    /**
     * 求包养对话框
     *
     * @param holder
     */
    private void nurtureChat(ViewHolder holder) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int size = (int)mContext.getResources().getDimension(R.dimen.gf_20dp);
        options.outWidth = size;
        options.outHeight = size;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.bjmgf_message_chat_nurture_title_pic, options);
        holder.rlImage.setBitmap(bitmap);
    }

    /**
     * 普通对话框
     *
     * @param msg
     * @param convertView
     * @param holder
     */
    private void normalChat(final int position, final Message msg, final View convertView, final ViewHolder holder) {
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
                if (mView.currentStatus == MessageView.Status.NORMAL) {
                    ToolTipView.getInstance().show(v, position, msg.getInfo(), msg.getType(), listener);
                }
                return true;
            }
        });

        holder.ivChat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mView.currentStatus == MessageView.Status.NORMAL) {
                    ToolTipView.getInstance().show(v, position, msg.getInfo(), msg.getType(), listener);
                }
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipView.getInstance().remove();
                if (mView.currentStatus == MessageView.Status.EDIT) {
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

        //求包养
        CustomRlImage rlImage;
    }
}
