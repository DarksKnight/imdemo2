package com.GF.platform.uikit.widget.emojitextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.GF.platform.uikit.widget.chatkeyboard.util.EmojiUtil;


public class EmojiTextView extends TextView{

    public EmojiTextView(Context context) {
        super(context);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        text = EmojiUtil.convert(getContext(), text.toString(), this);
        super.setText(text, type);
    }
}
