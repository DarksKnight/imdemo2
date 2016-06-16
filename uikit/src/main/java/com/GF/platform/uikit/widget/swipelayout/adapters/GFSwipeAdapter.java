package com.GF.platform.uikit.widget.swipelayout.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.GF.platform.uikit.widget.swipelayout.GFSwipeLayout;
import com.GF.platform.uikit.widget.swipelayout.implments.GFSwipeItemMangerImpl;
import com.GF.platform.uikit.widget.swipelayout.interfaces.GFSwipeAdapterInterface;
import com.GF.platform.uikit.widget.swipelayout.interfaces.GFSwipeItemMangerInterface;
import com.GF.platform.uikit.widget.swipelayout.util.GFAttributes;

import java.util.List;

public abstract class GFSwipeAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements GFSwipeItemMangerInterface, GFSwipeAdapterInterface {

    public GFSwipeItemMangerImpl mItemManger = new GFSwipeItemMangerImpl(this);

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH viewHolder, final int position);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(GFSwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<GFSwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(GFSwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public GFAttributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(GFAttributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
