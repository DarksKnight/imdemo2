package com.GF.platform.uikit.widget.chatkeyboard.base.adapter;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageSetEntity;

import java.util.ArrayList;

/**
 * Created by sunhaoyang on 2016/4/19.
 */
public class GFPageSetAdapter extends PagerAdapter {

    private final ArrayList<GFPageSetEntity> mGFPageSetEntityList = new ArrayList<>();

    public ArrayList<GFPageSetEntity> getPageSetEntityList() {
        return mGFPageSetEntityList;
    }

    public int getPageSetStartPosition(GFPageSetEntity gfPageSetEntity) {
        if (null == gfPageSetEntity || TextUtils.isEmpty(gfPageSetEntity.getUuid())) {
            return 0;
        }

        int startPosition = 0;
        for (int i = 0; i < mGFPageSetEntityList.size(); i++) {
            if (i == mGFPageSetEntityList.size() - 1 && !gfPageSetEntity.getUuid().equals(mGFPageSetEntityList.get(i).getUuid())) {
                return 0;
            }
            if (gfPageSetEntity.getUuid().equals(mGFPageSetEntityList.get(i).getUuid())) {
                return startPosition;
            }
            startPosition += mGFPageSetEntityList.get(i).getPageCount();
        }
        return startPosition;
    }

    public void add(View view) {
        add(mGFPageSetEntityList.size(), view);
    }

    public void add(int index, View view) {
        GFPageSetEntity gfPageSetEntity = new GFPageSetEntity.Builder()
                .addPageEntity(new GFPageEntity(view))
                .setShowIndicator(false)
                .build();
        mGFPageSetEntityList.add(index, gfPageSetEntity);
    }

    public void add(GFPageSetEntity gfPageSetEntity) {
        add(mGFPageSetEntityList.size(), gfPageSetEntity);
    }

    public void add(int index, GFPageSetEntity GFPageSetEntity) {
        if (GFPageSetEntity == null) {
            return;
        }
        mGFPageSetEntityList.add(index, GFPageSetEntity);
    }

    public GFPageSetEntity get(int position) {
        return mGFPageSetEntityList.get(position);
    }

    public GFPageEntity getPageEntity(int position) {
        for (GFPageSetEntity gfPageSetEntity : mGFPageSetEntityList) {
            if (gfPageSetEntity.getPageCount() > position) {
                return (GFPageEntity) gfPageSetEntity.getPageEntityList().get(position);
            } else {
                position -= gfPageSetEntity.getPageCount();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (GFPageSetEntity gfPageSetEntity : mGFPageSetEntityList) {
            count += gfPageSetEntity.getPageCount();
        }
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getPageEntity(position).instantiateItem(container, position, null);
        if(view == null){
            return null;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
