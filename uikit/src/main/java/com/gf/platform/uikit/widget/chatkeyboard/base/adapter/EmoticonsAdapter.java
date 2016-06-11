package com.GF.platform.uikit.widget.chatkeyboard.base.adapter;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.entity.Message;
import com.GF.platform.uikit.event.SendMessageEvent;
import com.GF.platform.uikit.util.Util;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.EmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.EmojiListener;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class EmoticonsAdapter extends CommonAdapter<EmoticonEntity> {
    private float oldLocationX = -1;
    private float oldLocationY = -1;
    private EmojiListener emojiListener = null;

    public EmoticonsAdapter(Context context, List<EmoticonEntity> data, EmojiListener emojiListener) {
        super(context, data, R.layout.bjmgf_message_item_emoticon_big);
        this.emojiListener = emojiListener;
    }

    @Override
    public void convert(ViewHolder viewHolder, final EmoticonEntity entity) {
        final RelativeLayout lyRoot = viewHolder.getView(R.id.ly_root);
        final SimpleDraweeView ivEmoticon = viewHolder.getView(R.id.iv_emoticon);
        TextView tvContent = viewHolder.getView(R.id.tv_content);
        if (entity.isShow()) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(entity.getShow());
        } else {
            tvContent.setVisibility(View.GONE);
        }

        if (entity.isEmoji()) {
            Uri uri = Uri.parse("asset://"+ context.getPackageName() + "/" + entity.getIconUri());
            ivEmoticon.setImageURI(uri);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ivEmoticon.getLayoutParams();
            ViewGroup.LayoutParams paramsRoot = lyRoot.getLayoutParams();
            paramsRoot.height = (int)context.getResources().getDimension(R.dimen.gf_40dp);
            if (entity.getId().equals("-1")) {
                params.width = (int)context.getResources().getDimension(R.dimen.gf_35dp);
                params.height = (int)context.getResources().getDimension(R.dimen.gf_25dp);
                params.setMargins(0, (int)context.getResources().getDimension(R.dimen.gf_7dp), 0, 0);
                ivEmoticon.setImageResource(R.mipmap.bjmgf_message_chat_del_face_noselected);
            } else {
                params.width = (int)context.getResources().getDimension(R.dimen.gf_40dp);
                params.height = (int)context.getResources().getDimension(R.dimen.gf_40dp);
            }
            lyRoot.setLayoutParams(paramsRoot);
            ivEmoticon.setLayoutParams(params);
        } else {
            Uri uri = Uri.parse("file://" + entity.getIconUri());
            ivEmoticon.setImageURI(uri);
        }

        lyRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("BJMEngine", "event.getAction() = " + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldLocationX = event.getX();
                        oldLocationY = event.getY();
                        if (entity.getName().equals("del_normal")) {
                            ivEmoticon.setImageResource(R.mipmap.bjmgf_message_chat_del_face_selected);
                            return true;
                        }
                        if (entity.isEmoji()) {
                            ivEmoticon.setBackgroundResource(R.drawable.bjmgf_message_chat_emoji_selected);
                        } else {
                            lyRoot.setBackgroundResource(R.drawable.bjmgf_message_chat_emoji_selected);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (entity.getName().equals("del_normal")) {
                            ivEmoticon.setImageResource(R.mipmap.bjmgf_message_chat_del_face_noselected);
                            return true;
                        }
                        if (entity.isEmoji()) {
                            ivEmoticon.setBackgroundResource(R.drawable.bjmgf_message_chat_emoji);
                        } else {
                            lyRoot.setBackgroundResource(R.drawable.bjmgf_message_chat_emoji);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //捕获单击事件
                        if (oldLocationX == event.getX() && oldLocationY == event.getY()) {
                            if (entity.isEmoji()) {
                                if (entity.getId().equals("-1")) {
                                    emojiListener.selectedBackSpace();
                                } else {
                                    emojiListener.selectedEmoji(entity);
                                }
                            } else {
                                Message message = new Message("帅的一般", "", Util.getDate(), "", Message.Category.NORMAL_ME,
                                        Util.getImageThumbnail(entity.getIconUri(), 200, 200), false);
                                EventBus.getDefault().post(new SendMessageEvent(message));
                            }
                        }
                        if (entity.getName().equals("del_normal")) {
                            ivEmoticon.setImageResource(R.mipmap.bjmgf_message_chat_del_face_noselected);
                            return true;
                        }
                        if (entity.isEmoji()) {
                            ivEmoticon.setBackgroundResource(R.drawable.bjmgf_message_chat_emoji);
                        } else {
                            lyRoot.setBackgroundResource(R.drawable.bjmgf_message_chat_emoji);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
