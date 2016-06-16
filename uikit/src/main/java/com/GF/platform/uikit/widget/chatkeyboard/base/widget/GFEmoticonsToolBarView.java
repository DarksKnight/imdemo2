package com.GF.platform.uikit.widget.chatkeyboard.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.GF.platform.uikit.R;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageSetEntity;

import java.util.ArrayList;

public class GFEmoticonsToolBarView extends RelativeLayout {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected ArrayList<View> mToolBtnList = new ArrayList<>();
    protected HorizontalScrollView hsv_toolbar;
    protected LinearLayout ly_tool;

    public GFEmoticonsToolBarView(Context context) {
        this(context, null);
    }

    public GFEmoticonsToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.bjmgf_message_chat_view_toolbar, this);
        this.mContext = context;
        hsv_toolbar = (HorizontalScrollView) findViewById(R.id.hsv_toolbar);
        ly_tool = (LinearLayout) findViewById(R.id.ly_tool);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() > 3) {
            throw new IllegalArgumentException("can host only two direct child");
        }
    }

    protected View getCommonItemToolBtn() {
        return mInflater == null ? null : mInflater.inflate(R.layout.bjmgf_message_chat_item_toolbtn, null);
    }

    protected void initItemToolBtn(View toolBtnView, int rec, final GFPageSetEntity GFPageSetEntity, OnClickListener onClickListener){
        SimpleDraweeView iv_icon = (SimpleDraweeView) toolBtnView.findViewById(R.id.iv_icon);
        if (rec > 0) {
            iv_icon.setImageResource(rec);
        }
        if (GFPageSetEntity != null) {
            iv_icon.setTag(R.id.id_tag_pageset, GFPageSetEntity);
            try {
                if (null != GFPageSetEntity.getIconUri() && GFPageSetEntity.getIconUri().trim().length() > 0) {
                    Bitmap bm = BitmapFactory.decodeFile(GFPageSetEntity.getIconUri());
                    iv_icon.setImageBitmap(bm);
                } else {
                    iv_icon.setImageDrawable(getResources().getDrawable(GFPageSetEntity.getIconId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        toolBtnView.setOnClickListener(onClickListener != null ? onClickListener : new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListeners != null && GFPageSetEntity != null) {
                    mItemClickListeners.onToolBarItemClick(GFPageSetEntity);
                }
            }
        });
    }

    protected View getToolBgBtn(View parentView) {
        return  parentView.findViewById(R.id.iv_icon);
    }

    public void addFixedToolItemView(boolean isRight, int rec, final GFPageSetEntity GFPageSetEntity, OnClickListener onClickListener) {
        View toolBtnView = getCommonItemToolBtn();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams hsvParams = (LayoutParams) hsv_toolbar.getLayoutParams();
        if (toolBtnView.getId() <= 0) {
            toolBtnView.setId(isRight ? R.id.id_toolbar_right : R.id.id_toolbar_left);
        }
        if (isRight) {
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, toolBtnView.getId());
        } else {
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF, toolBtnView.getId());
        }
        addView(toolBtnView, params);
        hsv_toolbar.setLayoutParams(hsvParams);
        initItemToolBtn(toolBtnView, rec, GFPageSetEntity, onClickListener);
    }

    public void addToolItemView(GFPageSetEntity GFPageSetEntity) {
        addToolItemView(0, GFPageSetEntity, null);
    }

    public void addToolItemView(int rec, final GFPageSetEntity GFPageSetEntity, OnClickListener onClickListener) {
        View toolBtnView = getCommonItemToolBtn();
        initItemToolBtn(toolBtnView, rec, GFPageSetEntity, onClickListener);
        ly_tool.addView(toolBtnView);
        mToolBtnList.add(getToolBgBtn(toolBtnView));
    }

    public void setToolBtnSelect(String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            return;
        }
        int select = 0;
        for (int i = 0; i < mToolBtnList.size(); i++) {
            Object object = mToolBtnList.get(i).getTag(R.id.id_tag_pageset);
            if (object != null && object instanceof GFPageSetEntity && uuid.equals(((GFPageSetEntity) object).getUuid())) {
                mToolBtnList.get(i).setBackgroundColor(getResources().getColor(R.color.gf_message_toolbar_btn_select));
                select = i;
            } else {
                mToolBtnList.get(i).setBackgroundResource(R.drawable.bjmgf_message_btn_toolbtn_bg);
            }
        }
        scrollToBtnPosition(select);
    }

    protected void scrollToBtnPosition(final int position) {
        int childCount = ly_tool.getChildCount();
        if (position < childCount) {
            hsv_toolbar.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = hsv_toolbar.getScrollX();

//                    int childX = (int) ViewHelper.getX(ly_tool.getChildAt(position));
                    int childX = (int)ly_tool.getChildAt(position).getX();

                    if (childX < mScrollX) {
                        hsv_toolbar.scrollTo(childX, 0);
                        return;
                    }

                    int childWidth = ly_tool.getChildAt(position).getWidth();
                    int hsvWidth = hsv_toolbar.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if (childRight > scrollRight) {
                        hsv_toolbar.scrollTo(childRight - scrollRight, 0);
                        return;
                    }
                }
            });
        }
    }

    protected OnToolBarItemClickListener mItemClickListeners;

    public interface OnToolBarItemClickListener {
        void onToolBarItemClick(GFPageSetEntity GFPageSetEntity);
    }

    public void setOnToolBarItemClickListener(OnToolBarItemClickListener listener) {
        this.mItemClickListeners = listener;
    }

    public void reset() {
        ly_tool.removeAllViews();
        mToolBtnList.clear();
    }
}

