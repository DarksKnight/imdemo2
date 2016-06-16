package com.GF.platform.gfplatform.ui.fragment.message;

import android.widget.LinearLayout;

import com.GF.platform.gfplatform.R;
import com.GF.platform.gfplatform.base.BaseFragment;
import com.GF.platform.uiview.messagelist.GFChatListGFView;

/**
 * 消息列表
 * Created by sunhaoyang on 2016/2/19.
 */
public class MessageFragment extends BaseFragment {
    private LinearLayout llMain = null;
    private GFChatListGFView v = null;

    @Override
    protected int getContentView() {
        return R.layout.bjmgf_message_fragment;
    }

    @Override
    protected void initView() {
        llMain = getView(R.id.bjmgf_message_content_ll);
        v = new GFChatListGFView(getActivity());
        llMain.addView(v);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        v.onResume();
    }
}
