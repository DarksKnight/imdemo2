package com.gf.platform.uikit.widget.chatkeyboard.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gf.platform.uikit.R;
import com.gf.platform.uikit.widget.chatkeyboard.base.entity.PageSetEntity;
import com.gf.platform.uikit.widget.chatkeyboard.util.KeyBoardUtil;

import java.util.ArrayList;

/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class EmoticonsIndicatorView extends LinearLayout {

    private static final int MARGIN_LEFT = 4;
    protected Context mContext;
    protected ArrayList<ImageView> mImageViews;
    protected Bitmap mBmpSelect;
    protected Bitmap mBmpNomal;
    protected LayoutParams mLeftLayoutParams;

    public EmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);

        mBmpSelect = BitmapFactory.decodeResource(getResources(), R.mipmap.bjmgf_message_chat_dotsselect);
        mBmpNomal = BitmapFactory.decodeResource(getResources(), R.mipmap.bjmgf_message_chat_dotsunselect);

        mLeftLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftLayoutParams.leftMargin = KeyBoardUtil.dip2px(context, MARGIN_LEFT);
    }

    public void playTo(int position, PageSetEntity pageSetEntity) {
        if (!checkPageSetEntity(pageSetEntity)) {
            return;
        }

        updateIndicatorCount(pageSetEntity.getPageCount());

        for (ImageView iv : mImageViews) {
            iv.setImageBitmap(mBmpNomal);
        }
        mImageViews.get(position).setImageBitmap(mBmpSelect);
    }

    public void playBy(int startPosition, int nextPosition, PageSetEntity pageSetEntity) {
        if (!checkPageSetEntity(pageSetEntity)) {
            return;
        }

        updateIndicatorCount(pageSetEntity.getPageCount());

        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }

        if (startPosition < 0) {
            startPosition = nextPosition = 0;
        }

        final ImageView imageViewStrat = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);

        imageViewStrat.setImageBitmap(mBmpNomal);
        imageViewNext.setImageBitmap(mBmpSelect);
    }

    protected boolean checkPageSetEntity(PageSetEntity pageSetEntity) {
        if (pageSetEntity != null && pageSetEntity.isShowIndicator()) {
            setVisibility(VISIBLE);
            return true;
        } else {
            setVisibility(GONE);
            return false;
        }
    }

    protected void updateIndicatorCount(int count) {
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        }
        if (count > mImageViews.size()) {
            for (int i = mImageViews.size(); i < count; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageBitmap(i == 0 ? mBmpSelect : mBmpNomal);
                this.addView(imageView, mLeftLayoutParams);
                mImageViews.add(imageView);
            }
        }
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
            } else {
                mImageViews.get(i).setVisibility(VISIBLE);
            }
        }
    }
}
