package com.GF.platform.uiview.messagelist.adapter;

import com.GF.platform.uikit.base.manager.message.MessageListControl;
import com.GF.platform.uikit.widget.chatkeyboard.util.EmojiUtil;
import com.GF.platform.uikit.widget.swipeback.SwipeBackActivityHelper;
import com.GF.platform.uikit.widget.swipelayout.SwipeLayout;
import com.GF.platform.uikit.widget.swipelayout.adapters.RecyclerSwipeAdapter;
import com.GF.platform.uiview.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sunhaoyang on 2016/2/22.
 */
public class MessageListAdapter extends RecyclerSwipeAdapter<MessageListAdapter.ViewHolder> {

    private Context mContext = null;
    private MsgAdapterListener mListener = null;
    private String targetPackageName = "com.GF.platform.gfplatform.ui.activity.message.MessageActivity";

    public MessageListAdapter(Context context, MsgAdapterListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bjmgf_message_list_view_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {

        view.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for(SwipeLayout s : mItemManger.getOpenLayouts()) {
                    if (s.getOpenStatus() == SwipeLayout.Status.Open) {
                        mItemManger.closeAllItems();
                        flag = true;
                    }
                }
                if (flag) {
                    return;
                }
                Intent intent = null;
                try {
                    intent = new Intent(mContext, Class.forName(targetPackageName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                intent.putExtras(bundle);
                SwipeBackActivityHelper.activityBuilder((Activity)mContext)
                        .intent(intent).needParallax(true).needBackgroundShadow(false).startActivity();
            }
        });

        view.tvNickName.setText(MessageListControl.getInstance().getMessage(position).getNickName());
        view.tvDate.setText(MessageListControl.getInstance().getMessage(position).getDate());
        view.sl.setClickToClose(true);

        view.rlContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.rlContent.setBackgroundColor(mContext.getResources().getColor(R.color.gf_message_swipe_action_down));
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    view.rlContent.setBackgroundColor(mContext.getResources().getColor(R.color.gf_white));
                }
                return false;
            }
        });

        if (MessageListControl.getInstance().getMessage(position).getDraft().trim().length() > 0) {
            view.tvInfo.setText(EmojiUtil.convertDraft(mContext, "[草稿]" + MessageListControl.getInstance().getMessage(position).getDraft(), view.tvInfo));
        } else {
            view.tvInfo.setText(MessageListControl.getInstance().getMessage(position).getInfo());
        }

        if (!MessageListControl.getInstance().getMessage(position).isTop()) {
            view.tvTop.setText("消息置顶");
            view.rlContent.setBackgroundColor(mContext.getResources().getColor(R.color.gf_white));
        } else {
            view.tvTop.setText("取消置顶");
            view.rlContent.setBackgroundColor(mContext.getResources().getColor(R.color.gf_message_top_bg));
        }

        view.rlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnMessageTop(position);
                mItemManger.removeShownLayouts(view.sl);
                mItemManger.closeAllItems();
            }
        });
        view.rlDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.closeItem(position);
                mItemManger.removeShownLayouts(view.sl);
                mItemManger.closeAllItems();
                mListener.OnMessageDel(position);
            }
        });

        mItemManger.bind(view.itemView, position);
    }

    @Override
    public int getItemCount() {
        return MessageListControl.getInstance().getMessageSize();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.bjmgf_message_item_sl;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlContent = null;
        public SwipeLayout sl = null;
        public RelativeLayout rlTop = null;
        public RelativeLayout rlDel = null;
        public TextView tvNickName = null;
        public TextView tvInfo = null;
        public TextView tvDate = null;
        public TextView tvTop = null;
        public View view = null;

        public ViewHolder(View v) {
            super(v);
            rlContent = (RelativeLayout) v.findViewById(R.id.bjmgf_message_item_rl);
            sl = (SwipeLayout) v.findViewById(R.id.bjmgf_message_item_sl);
            rlTop = (RelativeLayout) v.findViewById(R.id.bjmgf_message_swipe_top_rl);
            rlDel = (RelativeLayout) v.findViewById(R.id.bjmgf_message_swipe_del_rl);
            tvNickName = (TextView) v.findViewById(R.id.bjmgf_message_top_nickname);
            tvInfo = (TextView) v.findViewById(R.id.bjmgf_message_bottom_info);
            tvDate = (TextView) v.findViewById(R.id.bjmgf_message_right_date);
            tvTop = (TextView) v.findViewById(R.id.bjmgf_message_swip_top_tv);
            view = v;
        }
    }

    public interface MsgAdapterListener {
        void OnMessageTop(int position);
        void OnMessageDel(int position);
    }

}
