package com.gf.platform.gfplatform;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/2/19.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mListFragments = null;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> listFragments) {
        super(fm);
        mListFragments = listFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }
}
