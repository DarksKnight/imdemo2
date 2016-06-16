package com.GF.platform.uikit.widget.chatkeyboard.base.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.GF.platform.uikit.R;


/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class GFEmoticonPageView extends RelativeLayout {

    private GridView mGvEmotion;

    public GridView getEmoticonsGridView() {
        return mGvEmotion;
    }

    public GFEmoticonPageView(Context context) {
        this(context, null);
    }

    public GFEmoticonPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_chat_item_facepage, this);
        mGvEmotion = (GridView) view.findViewById(R.id.bjmgf_message_gv_emotion);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            mGvEmotion.setMotionEventSplittingEnabled(false);
        }
        mGvEmotion.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mGvEmotion.setCacheColorHint(0);
        mGvEmotion.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGvEmotion.setVerticalScrollBarEnabled(false);
    }

    public void setNumColumns(int row) {
        mGvEmotion.setNumColumns(row);
    }
}
