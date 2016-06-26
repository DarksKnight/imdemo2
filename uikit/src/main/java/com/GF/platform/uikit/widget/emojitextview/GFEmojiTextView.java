package com.GF.platform.uikit.widget.emojitextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.GF.platform.uikit.widget.chatkeyboard.util.GFEmojiUtil;


public class GFEmojiTextView extends TextView{

    public GFEmojiTextView(Context context) {
        super(context);
    }

    public GFEmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GFEmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        text = GFEmojiUtil.convert(getContext(), text.toString(), this);
        super.setText(text, type);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
