package com.GF.platform.uikit.widget.audioview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.widget.circleview.GFCircleView;

/**
 * Created by sunhaoyang on 2016/6/15.
 */
public class GFAudioRecordView extends LinearLayout {

    public GFAudioRecordView(Context context) {
        super(context);

        init();
    }

    public GFAudioRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GFAudioRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bjmgf_message_chat_audio_record_voice_left, this);
    }

    public void setVolumn(int volumn) {
        LinearLayout root = (LinearLayout) getChildAt(0);
        LinearLayout[] llArray = reload();
        for (int i = 0; i < volumn; i++) {
            int color = Color.rgb(252, 121 + i * 21, 9 + i * 40);
            for (int j = 0; j < llArray[root.getChildCount() - 1 - i].getChildCount(); j++) {
                GFCircleView view = (GFCircleView) llArray[root.getChildCount() - 1 - i].getChildAt(j);
                view.setViewColor(color);
            }
        }
    }

    public LinearLayout[] reload() {
        LinearLayout root = (LinearLayout) getChildAt(0);
        LinearLayout[] llArray = new LinearLayout[6];
        for (int i = 0; i < root.getChildCount(); i++) {
            llArray[i] = (LinearLayout) root.getChildAt(i);
        }
        for (int i = 0; i < llArray.length; i++) {
            for (int j = 0; j < llArray[i].getChildCount(); j++) {
                GFCircleView view = (GFCircleView) llArray[i].getChildAt(j);
                view.setViewColor(Color.WHITE);
            }
        }
        return llArray;
    }
}
