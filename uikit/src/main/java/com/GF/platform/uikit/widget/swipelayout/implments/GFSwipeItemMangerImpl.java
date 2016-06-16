package com.GF.platform.uikit.widget.swipelayout.implments;

import android.view.View;

import com.GF.platform.uikit.widget.swipelayout.GFSwipeListener;
import com.GF.platform.uikit.widget.swipelayout.GFSwipeLayout;
import com.GF.platform.uikit.widget.swipelayout.interfaces.GFSwipeAdapterInterface;
import com.GF.platform.uikit.widget.swipelayout.interfaces.GFSwipeItemMangerInterface;
import com.GF.platform.uikit.widget.swipelayout.util.GFAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SwipeItemMangerImpl is a helper class to help all the adapters to maintain open status.
 */
public class GFSwipeItemMangerImpl implements GFSwipeItemMangerInterface {

    private GFAttributes.Mode mode = GFAttributes.Mode.Single;
    public final int INVALID_POSITION = -1;

    protected int mOpenPosition = INVALID_POSITION;

    protected Set<Integer> mOpenPositions = new HashSet<Integer>();
    protected Set<GFSwipeLayout> mShownLayouts = new HashSet<GFSwipeLayout>();

    protected GFSwipeAdapterInterface GFSwipeAdapterInterface;

    public GFSwipeItemMangerImpl(GFSwipeAdapterInterface GFSwipeAdapterInterface) {
        if (GFSwipeAdapterInterface == null)
            throw new IllegalArgumentException("SwipeAdapterInterface can not be null");

        this.GFSwipeAdapterInterface = GFSwipeAdapterInterface;
    }

    public GFAttributes.Mode getMode() {
        return mode;
    }

    public void setMode(GFAttributes.Mode mode) {
        this.mode = mode;
        mOpenPositions.clear();
        mShownLayouts.clear();
        mOpenPosition = INVALID_POSITION;
    }

    public void bind(View view, int position) {
        int resId = GFSwipeAdapterInterface.getSwipeLayoutResourceId(position);
        GFSwipeLayout GFSwipeLayout = (GFSwipeLayout) view.findViewById(resId);
        if (GFSwipeLayout == null)
            throw new IllegalStateException("can not find SwipeLayout in target view");

        if (GFSwipeLayout.getTag(resId) == null) {
            OnLayoutListener onLayoutListener = new OnLayoutListener(position);
            SwipeMemory swipeMemory = new SwipeMemory(position);
            GFSwipeLayout.addSwipeListener(swipeMemory);
            GFSwipeLayout.addOnLayoutListener(onLayoutListener);
            GFSwipeLayout.setTag(resId, new ValueBox(position, swipeMemory, onLayoutListener));
            mShownLayouts.add(GFSwipeLayout);
        } else {
            ValueBox valueBox = (ValueBox) GFSwipeLayout.getTag(resId);
            valueBox.swipeMemory.setPosition(position);
            valueBox.onLayoutListener.setPosition(position);
            valueBox.position = position;
        }
    }

    @Override
    public void openItem(int position) {
        if (mode == GFAttributes.Mode.Multiple) {
            if (!mOpenPositions.contains(position))
                mOpenPositions.add(position);
        } else {
            mOpenPosition = position;
        }
        GFSwipeAdapterInterface.notifyDatasetChanged();
    }

    @Override
    public void closeItem(int position) {
        if (mode == GFAttributes.Mode.Multiple) {
            mOpenPositions.remove(position);
        } else {
            if (mOpenPosition == position)
                mOpenPosition = INVALID_POSITION;
        }
        GFSwipeAdapterInterface.notifyDatasetChanged();
    }

    @Override
    public void closeAllExcept(GFSwipeLayout layout) {
        for (GFSwipeLayout s : mShownLayouts) {
            if (s != layout)
                s.close();
        }
    }

    @Override
    public void closeAllItems() {
        if (mode == GFAttributes.Mode.Multiple) {
            mOpenPositions.clear();
        } else {
            mOpenPosition = INVALID_POSITION;
        }
        for (GFSwipeLayout s : mShownLayouts) {
            s.close();
        }
    }

    @Override
    public void removeShownLayouts(GFSwipeLayout layout) {
        mShownLayouts.remove(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        if (mode == GFAttributes.Mode.Multiple) {
            return new ArrayList<Integer>(mOpenPositions);
        } else {
            return Collections.singletonList(mOpenPosition);
        }
    }

    @Override
    public List<GFSwipeLayout> getOpenLayouts() {
        return new ArrayList<GFSwipeLayout>(mShownLayouts);
    }

    @Override
    public boolean isOpen(int position) {
        if (mode == GFAttributes.Mode.Multiple) {
            return mOpenPositions.contains(position);
        } else {
            return mOpenPosition == position;
        }
    }

    class ValueBox {
        OnLayoutListener onLayoutListener;
        SwipeMemory swipeMemory;
        int position;

        ValueBox(int position, SwipeMemory swipeMemory, OnLayoutListener onLayoutListener) {
            this.swipeMemory = swipeMemory;
            this.onLayoutListener = onLayoutListener;
            this.position = position;
        }
    }

    class OnLayoutListener implements GFSwipeLayout.OnLayout {

        private int position;

        OnLayoutListener(int position) {
            this.position = position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onLayout(GFSwipeLayout v) {
            if (isOpen(position)) {
                v.open(false, false);
            } else {
                v.close(false, false);
            }
        }

    }

    class SwipeMemory extends GFSwipeListener {

        private int position;

        SwipeMemory(int position) {
            this.position = position;
        }

        @Override
        public void onClose(GFSwipeLayout layout) {
            if (mode == GFAttributes.Mode.Multiple) {
                mOpenPositions.remove(position);
            } else {
                mOpenPosition = INVALID_POSITION;
            }
        }

        @Override
        public void onStartOpen(GFSwipeLayout layout) {
            if (mode == GFAttributes.Mode.Single) {
                closeAllExcept(layout);
            }
        }

        @Override
        public void onOpen(GFSwipeLayout layout) {
            if (mode == GFAttributes.Mode.Multiple)
                mOpenPositions.add(position);
            else {
                closeAllExcept(layout);
                mOpenPosition = position;
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}
