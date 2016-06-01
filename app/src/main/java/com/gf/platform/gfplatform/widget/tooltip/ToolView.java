package com.gf.platform.gfplatform.widget.tooltip;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gf.platform.gfplatform.R;
import com.gf.platform.gfplatform.entity.Message;
import com.gf.platform.gfplatform.util.LogProxy;
import com.gf.platform.gfplatform.util.Util;

/**
 * 消息长按复制删除工具提示view
 * Created by sunhaoyang on 2016/5/3.
 */
public class ToolView extends LinearLayout implements View.OnClickListener {

    private TextView tvCopy = null;
    private TextView tvRelay = null;
    private TextView tvDel = null;
    private TextView tvMore = null;
    private ImageView ivArrowDown = null;
    private ImageView ivArrowUp = null;
    private Direction currentDirection = Direction.DOWN;
    private Type currentType = Type.TEXT;
    private Message message = null;
    private ControlListener listener = null;
    //对话框显示的方向
    public enum Direction {
        UP, DOWN;
    }
    //输入的内容（文字，表情）
    public enum Type {
        TEXT, EMOTICON;
    }

    public ToolView(Context context) {
        super(context);
        //拿到布局解析器对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bjmgf_message_chat_tool_tip, this);
        //解析布局中的控件
        tvCopy = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_copy);
        tvRelay = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_relay);
        tvDel = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_del);
        tvMore = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_more);
        ivArrowDown = (ImageView) findViewById(R.id.bjmgf_message_chat_tool_tip_arrow_down);
        ivArrowUp = (ImageView) findViewById(R.id.bjmgf_message_chat_tool_tip_arrow_up);

        tvCopy.setOnClickListener(this);
        tvRelay.setOnClickListener(this);
        tvDel.setOnClickListener(this);
        tvMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bjmgf_message_chat_tool_tip_copy:
                LogProxy.i("copy");
                copy();
                break;
            case R.id.bjmgf_message_chat_tool_tip_relay:
                LogProxy.i("relay");
                break;
            case R.id.bjmgf_message_chat_tool_tip_del:
                LogProxy.i("del");
                if (null != listener) {
                    listener.del(message);
                }
                break;
            case R.id.bjmgf_message_chat_tool_tip_more:
                LogProxy.i("more");
                if (null != listener) {
                    listener.more(message);
                }
                break;
            default:
                break;
        }
        ToolTipView.getInstance().remove();
    }

    /**
     * 复制对应消息至系统剪切板中
     */
    private void copy() {
        //调用系统剪切板服务
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", message.getInfo());
        clipboardManager.setPrimaryClip(clipData);
        Util.showToast(getContext(), getResources().getString(R.string.bjmgf_message_chat_copy_success));
    }

    /**
     * 控制对话框的位置
     */
    public void setArrowLocation(int Leftmargin, int rightMargin) {
        MarginLayoutParams params = (MarginLayoutParams) ivArrowDown.getLayoutParams();
        params.setMargins(Leftmargin, 0, rightMargin, 0);
        if (currentDirection == Direction.DOWN) {
            ivArrowDown.setLayoutParams(params);
        } else {
            ivArrowUp.setLayoutParams(params);
        }
    }

    /**
     *
     * 控制对话框的方向
     */
    public void setDirection(Direction direction) {
        currentDirection = direction;
        if (direction == Direction.UP) {
            ivArrowDown.setVisibility(View.GONE);
            ivArrowUp.setVisibility(View.VISIBLE);
        } else {
            ivArrowDown.setVisibility(View.VISIBLE);
            ivArrowUp.setVisibility(View.GONE);
        }
    }

    public void setType(Type type) {
        currentType = type;
        if (type == Type.TEXT) {
            tvCopy.setVisibility(View.VISIBLE);
            tvRelay.setBackgroundResource(R.color.gf_message_tool_tip_bg);
        } else {
            tvCopy.setVisibility(View.GONE);
            tvRelay.setBackgroundResource(R.drawable.bjmgf_message_chat_tool_tip_bg_left);
        }
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setListener(ControlListener listener) {
        this.listener = listener;
    }

    public interface ControlListener {
        void del(Message message);
        void more(Message message);
    }
}
