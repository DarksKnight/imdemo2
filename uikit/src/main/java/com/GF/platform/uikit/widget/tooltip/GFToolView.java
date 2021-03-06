package com.GF.platform.uikit.widget.tooltip;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.util.GFUtil;


/**
 * 消息长按复制删除工具提示view
 * Created by sunhaoyang on 2016/5/3.
 */
public class GFToolView extends LinearLayout implements View.OnClickListener {

    private TextView tvCopy = null;
    private TextView tvRelay = null;
    private TextView tvDel = null;
    private TextView tvMore = null;
    private TextView tvListen = null;
    private TextView tvSaveDisk = null;
    private ImageView ivArrowDown = null;
    private ImageView ivArrowUp = null;
    private View vSaveDiskLine = null;
    private View vCopyLine = null;
    private Direction currentDirection = Direction.DOWN;
    private Type currentType = Type.TEXT;
    private GFMessage gfMessage = null;
    private ControlListener listener = null;
    //对话框显示的方向
    public enum Direction {
        UP, DOWN;
    }
    //输入的内容（文字，表情）
    public enum Type {
        TEXT, EMOTICON, VOICE, PIC
    }

    public GFToolView(Context context) {
        super(context);
        //拿到布局解析器对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bjmgf_message_chat_tool_tip, this);
        //解析布局中的控件
        tvCopy = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_copy);
        tvRelay = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_relay);
        tvDel = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_del);
        tvMore = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_more);
        tvListen = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_voice_listen);
        tvSaveDisk = (TextView) findViewById(R.id.bjmgf_message_chat_tool_tip_save_disk);
        vSaveDiskLine = findViewById(R.id.bjmgf_message_chat_tool_tip_save_disk_line);
        vCopyLine = findViewById(R.id.bjmgf_message_chat_tool_tip_copy_line);
        ivArrowDown = (ImageView) findViewById(R.id.bjmgf_message_chat_tool_tip_arrow_down);
        ivArrowUp = (ImageView) findViewById(R.id.bjmgf_message_chat_tool_tip_arrow_up);

        tvCopy.setOnClickListener(this);
        tvRelay.setOnClickListener(this);
        tvDel.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        tvSaveDisk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bjmgf_message_chat_tool_tip_copy) {
            copy();
        } else if (id == R.id.bjmgf_message_chat_tool_tip_relay) {

        } else if (id == R.id.bjmgf_message_chat_tool_tip_del) {
            if (null != listener) {
                listener.del(gfMessage);
            }
        } else if (id == R.id.bjmgf_message_chat_tool_tip_more) {
            if (null != listener) {
                listener.more(gfMessage);
            }
        } else if (id == R.id.bjmgf_message_chat_tool_tip_save_disk) {
            saveDisk();
        }
        GFToolTipView.getInstance().remove();
    }

    /**
     * 复制对应消息至系统剪切板中
     */
    private void copy() {
        //调用系统剪切板服务
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", gfMessage.getInfo());
        clipboardManager.setPrimaryClip(clipData);
        GFUtil.showToast(getContext(), getResources().getString(R.string.bjmgf_message_chat_copy_success));
    }

    private void saveDisk() {
        GFUtil.saveImageToGallery(getContext(), gfMessage.getPath());
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
        if (currentType == Type.TEXT) {
            tvCopy.setVisibility(View.VISIBLE);
            tvRelay.setBackgroundResource(R.color.gf_message_tool_tip_bg);
            reset();
        } else if (currentType == Type.VOICE) {
            tvListen.setVisibility(View.VISIBLE);
            tvCopy.setVisibility(View.GONE);
            tvRelay.setVisibility(View.GONE);
        } else if (currentType == Type.PIC) {
            tvCopy.setVisibility(View.GONE);
            tvRelay.setBackgroundResource(R.drawable.bjmgf_message_chat_tool_tip_bg_left);
            tvSaveDisk.setVisibility(View.VISIBLE);
            vSaveDiskLine.setVisibility(View.VISIBLE);
            vCopyLine.setVisibility(View.GONE);
        } else {
            tvCopy.setVisibility(View.GONE);
            tvRelay.setBackgroundResource(R.drawable.bjmgf_message_chat_tool_tip_bg_left);
            reset();
        }
    }

    private void reset() {
        tvListen.setVisibility(View.GONE);
        tvCopy.setVisibility(View.VISIBLE);
        tvRelay.setVisibility(View.VISIBLE);
        tvSaveDisk.setVisibility(View.GONE);
        vSaveDiskLine.setVisibility(View.GONE);
        vCopyLine.setVisibility(View.VISIBLE);
    }

    public void setGfMessage(GFMessage gfMessage) {
        this.gfMessage = gfMessage;
    }

    public void setListener(ControlListener listener) {
        this.listener = listener;
    }

    public interface ControlListener {
        void del(GFMessage GFMessage);
        void more(GFMessage GFMessage);
    }
}
