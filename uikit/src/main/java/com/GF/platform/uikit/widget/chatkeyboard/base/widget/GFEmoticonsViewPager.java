package com.GF.platform.uikit.widget.chatkeyboard.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.GFPageSetAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageSetEntity;


/**
 * Created by sunhaoyang on 2016/4/19.
 */
public class GFEmoticonsViewPager extends ViewPager {

    protected GFPageSetAdapter mGFPageSetAdapter;
    protected int mCurrentPagePosition;

    public GFEmoticonsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(GFPageSetAdapter adapter) {
        super.setAdapter(adapter);
        this.mGFPageSetAdapter = adapter;

        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                checkPageChange(position);
                mCurrentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mOnEmoticonsPageViewListener == null
                || mGFPageSetAdapter.getPageSetEntityList().isEmpty()) {
            return;
        }
        GFPageSetEntity GFPageSetEntity = mGFPageSetAdapter.getPageSetEntityList().get(0);
        mOnEmoticonsPageViewListener.playTo(0, GFPageSetEntity);
        mOnEmoticonsPageViewListener.emoticonSetChanged(GFPageSetEntity);
    }

    public void setCurrentPageSet(GFPageSetEntity GFPageSetEntity) {
        if (mGFPageSetAdapter == null || mGFPageSetAdapter.getCount() <= 0) {
            return;
        }
        setCurrentItem(mGFPageSetAdapter.getPageSetStartPosition(GFPageSetEntity));
    }

    public void checkPageChange(int position) {
        if (mGFPageSetAdapter == null) {
            return;
        }
        int end = 0;
        for (GFPageSetEntity GFPageSetEntity : mGFPageSetAdapter.getPageSetEntityList()) {

            int size = GFPageSetEntity.getPageCount();

            if (end + size > position) {

                boolean isEmoticonSetChanged = true;
                // 上一表情集
                if (mCurrentPagePosition - end >= size) {
                    if (mOnEmoticonsPageViewListener != null) {
                        mOnEmoticonsPageViewListener.playTo(position - end, GFPageSetEntity);
                    }
                }
                // 下一表情集
                else if (mCurrentPagePosition - end < 0) {
                    if (mOnEmoticonsPageViewListener != null) {
                        mOnEmoticonsPageViewListener.playTo(0, GFPageSetEntity);
                    }
                }
                // 当前表情集
                else {
                    if (mOnEmoticonsPageViewListener != null) {
                        mOnEmoticonsPageViewListener.playBy(mCurrentPagePosition - end, position - end, GFPageSetEntity);
                    }
                    isEmoticonSetChanged = false;
                }

                if (isEmoticonSetChanged && mOnEmoticonsPageViewListener != null) {
                    mOnEmoticonsPageViewListener.emoticonSetChanged(GFPageSetEntity);
                }
                return;
            }
            end += size;
        }
    }

    private OnEmoticonsPageViewListener mOnEmoticonsPageViewListener;

    public void setOnIndicatorListener(OnEmoticonsPageViewListener listener) {
        mOnEmoticonsPageViewListener = listener;
    }

    public interface OnEmoticonsPageViewListener {

        void emoticonSetChanged(GFPageSetEntity GFPageSetEntity);

        /**
         * @param position 相对于当前表情集的位置
         */
        void playTo(int position, GFPageSetEntity GFPageSetEntity);

        /**
         * @param oldPosition 相对于当前表情集的始点位置
         * @param newPosition 相对于当前表情集的终点位置
         */
        void playBy(int oldPosition, int newPosition, GFPageSetEntity GFPageSetEntity);
    }
}
