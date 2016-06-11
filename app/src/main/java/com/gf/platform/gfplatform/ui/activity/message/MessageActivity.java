package com.GF.platform.gfplatform.ui.activity.message;

import com.GF.platform.gfplatform.R;
import com.GF.platform.gfplatform.base.BaseFragmentActivity;
import com.GF.platform.uiview.message.MessageView;

import android.widget.LinearLayout;


/**
 * im界面
 * Created by sunhaoyang on 2016/2/23.
 */
public class MessageActivity extends BaseFragmentActivity {
    private LinearLayout llMain = null;
    private MessageView v = null;

    @Override
    protected int getContentView() {
        return R.layout.bjmgf_message_activity;
    }

    @Override
    protected void initView() {
        llMain = getView(R.id.bjmgf_message_chat_content_ll);
        MessageView.index = getIntent().getIntExtra("index", -1);
        v = new MessageView(this);
        llMain.addView(v);
        setTitleText(v.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        v.onResume();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void finish() {
        super.finish();
        v.finish();
    }
}
