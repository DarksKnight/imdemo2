package com.GF.platform.gfplatform.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.GF.platform.gfplatform.R;

/**
 * Created by sunhaoyang on 2016/2/19.
 */
public class GameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bjmgf_game_fragment, container, false);
    }
}
