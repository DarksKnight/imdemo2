package com.GF.platform.uiview.message.adapter;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.base.manager.message.GFMessageControl;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFKeyBoardPorts;
import com.GF.platform.uikit.widget.circleimageview.GFCircleImageView;
import com.GF.platform.uikit.widget.customimage.GFCustomRlImage;
import com.GF.platform.uikit.widget.emojitextview.GFEmojiTextView;
import com.GF.platform.uikit.widget.tooltip.GFToolTipView;
import com.GF.platform.uikit.widget.tooltip.GFToolView;
import com.GF.platform.uiview.R;
import com.GF.platform.uiview.message.GFChatRoomView;
import com.facebook.drawee.view.SimpleDraweeView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/6/8.
 */

public class GFChatRoomAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<GFMessage> mList = null;
    private GFToolView.ControlListener mListener = null;
    private GFChatRoomView mView = null;
    private GFKeyBoardPorts mPorts = null;

    //自定义将视图放置对应位置
    private GFToolTipView.ViewInstall<GFToolView> toolTipViewInstall = new GFToolTipView.ViewInstall<GFToolView>() {
        @Override
        public void doShow(GFToolView view, ListView listView, View anchor, Object... objs) {
            ViewGroup rootView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
            int toolViewWidth = (int) mContext.getResources().getDimension(R.dimen.gf_205dp);
            int toolViewHeight = (int) mContext.getResources().getDimension(R.dimen.gf_40dp);
            rootView.removeView(view);
            rootView.addView(view);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            int[] listViewLocation = new int[2];
            listView.getLocationOnScreen(listViewLocation);
            int listViewY = listViewLocation[1];
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];

            int width = anchor.getMeasuredWidth() - toolViewWidth;
            int leftMargin = x + width / 2;
            int topMargin = 0;

            if ((y - listViewY) < toolViewHeight) {
                view.setDirection(GFToolView.Direction.UP);
                topMargin = y + anchor.getMeasuredHeight();
            } else {
                view.setDirection(GFToolView.Direction.DOWN);
                topMargin = y - toolViewHeight;
            }

            int screenWidth = GFUtil.getScreenWidth(mContext);
            int distance = toolViewWidth - screenWidth / 2;

            if (leftMargin > 0 && leftMargin + distance > screenWidth / 2) {
                if (screenWidth / 2 < toolViewWidth) {
                    leftMargin -= distance;
                    view.setArrowLocation(distance, 0);
                }
            } else if (leftMargin < 0) {
                leftMargin += distance;
                view.setArrowLocation(0, distance);
            }

            params.setMargins(leftMargin, topMargin, 0, 0);
            view.setLayoutParams(params);

            GFMessage message = (GFMessage) objs[0];
            view.setGfMessage(message);
            if (message.getType() == GFConstant.MSG_TYPE_EXPRESSION) {
                view.setType(GFToolView.Type.EMOTICON);
            } else if (message.getType() == GFConstant.MSG_TYPE_TEXT) {
                view.setType(GFToolView.Type.TEXT);
            } else if (message.getType() == GFConstant.MSG_TYPE_AUDIO) {
                view.setType(GFToolView.Type.VOICE);
            }
            view.setListener((GFToolView.ControlListener) objs[1]);
        }

        @Override
        public void doRemove(View view) {
            ViewGroup rootView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
            rootView.removeView(view);
        }
    };

    public GFChatRoomAdapter(Context context, GFMessageControl GFMessageControl, GFToolView.ControlListener listener, GFChatRoomView view, GFKeyBoardPorts ports) {
        mContext = context;
        mList = GFMessageControl.getGFMessages();
        mListener = listener;
        mView = view;
        mPorts = ports;
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
        if (mList.get(position).getCategory() == GFMessage.Category.NURTURE) {
            return 2;
        } else if (mList.get(position).getCategory() == GFMessage.Category.NORMAL_ME) {
            return 1;
        } else if (mList.get(position).getCategory() == GFMessage.Category.NORMAL_YOU) {
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
        GFMessage msg = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if (msg.getCategory() == GFMessage.Category.NORMAL_ME || msg.getCategory() == GFMessage.Category.NORMAL_YOU) {
                if (msg.getCategory() == GFMessage.Category.NORMAL_ME) {
                    convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_me_item, null);
                } else if (msg.getCategory() == GFMessage.Category.NORMAL_YOU) {
                    convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_you_item, null);
                }
                holder.tvChat = (GFEmojiTextView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_tv);
                holder.tvTime = (TextView) convertView.findViewById(R.id.bjmgf_message_chat_time_tv);
                holder.ivFace = (GFCircleImageView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_face_iv);
                holder.ivChat = (ImageView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_expression);
                holder.rl = (RelativeLayout) convertView.findViewById(R.id.bjmgf_message_chat_root);
                holder.ivSelect = (ImageView) convertView.findViewById(R.id.bjmgf_message_chat_list_select);
                holder.sdvPic = (SimpleDraweeView) convertView.findViewById(R.id.bjmgf_message_chat_msg_item_picture);
                if (msg.getCategory() == GFMessage.Category.NORMAL_ME) {
                    holder.ivLoading = (ImageView) convertView.findViewById(R.id.bjmgf_message_chat_loading);
                    AnimationDrawable progressAnim = (AnimationDrawable) holder.ivLoading.getDrawable();
                    progressAnim.start();
                }
            } else if (msg.getCategory() == GFMessage.Category.NURTURE) {
                convertView = View.inflate(mContext, R.layout.bjmgf_message_chat_list_msg_info_nurture_item, null);
                holder.rlImage = (GFCustomRlImage) convertView.findViewById(R.id.bjmgf_message_chat_nurture_bg);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (msg.getCategory() == GFMessage.Category.NORMAL_ME || msg.getCategory() == GFMessage.Category.NORMAL_YOU) {
            normalChat(msg, convertView, holder);
        } else if (msg.getCategory() == GFMessage.Category.NURTURE) {
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
        int size = (int) mContext.getResources().getDimension(R.dimen.gf_20dp);
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
    private void normalChat(final GFMessage msg, final View convertView, final ViewHolder holder) {
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
            holder.sdvPic.setVisibility(View.GONE);
            resetView(holder);
        } else if (msg.getPicture().trim().length() > 0) {
            holder.sdvPic.setVisibility(View.VISIBLE);
            holder.tvChat.setVisibility(View.GONE);
            holder.sdvPic.setImageURI(Uri.parse("file://" + msg.getPicture()));
        } else if (msg.getAudioTime() > 0) {
            float second = msg.getAudioTime() / 1000;
            if (msg.getCategory() == GFMessage.Category.NORMAL_ME) {
                holder.tvChat.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bjmgf_message_chat_me_voice_max, 0);
                holder.tvChat.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                holder.tvChat.setText((int) second + "\"     ");
            } else {
                holder.tvChat.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bjmgf_message_chat_you_voice_max, 0, 0, 0);
                holder.tvChat.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                holder.tvChat.setText("     " + (int) second + "\"");
            }
            holder.ivChat.setVisibility(View.GONE);
            holder.tvChat.setVisibility(View.VISIBLE);
            holder.sdvPic.setVisibility(View.GONE);
        } else {
            holder.tvChat.setText(msg.getInfo());
            holder.ivChat.setVisibility(View.GONE);
            holder.tvChat.setVisibility(View.VISIBLE);
            holder.sdvPic.setVisibility(View.GONE);
            resetView(holder);
        }

        holder.tvTime.setText(msg.getDate());

        if (msg.getCategory() == GFMessage.Category.NORMAL_ME) {
            if (msg.isSending()) {
                holder.ivLoading.setVisibility(View.VISIBLE);
            } else {
                holder.ivLoading.setVisibility(View.GONE);
            }
        }

        holder.tvChat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mView.currentStatus == GFChatRoomView.Status.NORMAL) {
                    GFToolTipView.getInstance().show(v, msg, mListener, toolTipViewInstall);
                }
                return true;
            }
        });

        holder.ivChat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mView.currentStatus == GFChatRoomView.Status.NORMAL) {
                    GFToolTipView.getInstance().show(v, msg, mListener, toolTipViewInstall);
                }
                return true;
            }
        });

        holder.tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getAudioTime() > 0) {

                }
                GFToolTipView.getInstance().remove();
                mPorts.reset();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GFToolTipView.getInstance().remove();
                mPorts.reset();
                if (mView.currentStatus == GFChatRoomView.Status.EDIT) {
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

    private void resetView(ViewHolder holder) {
        holder.tvChat.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ViewGroup.LayoutParams lp = holder.tvChat.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.tvChat.setLayoutParams(lp);
        holder.tvChat.setGravity(Gravity.LEFT);
    }

    public static class ViewHolder {
        //对话（包含你和我）
        public TextView tvTime;
        public GFEmojiTextView tvChat;
        public ImageView ivChat;
        public GFCircleImageView ivFace;
        public RelativeLayout rl;
        public ImageView ivSelect;
        public ImageView ivLoading;
        public SimpleDraweeView sdvPic;

        //求包养
        public GFCustomRlImage rlImage;
    }
}
