package com.GF.platform.gfplatform.ui.activity.message;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.GF.platform.gfplatform.R;
import com.GF.platform.gfplatform.base.BaseFragmentActivity;
import com.GF.platform.uikit.widget.swipeback.GFSwipeBackActivityHelper;
import com.GF.platform.uiview.message.GFChatRoomGFView;


/**
 * im界面
 * Created by sunhaoyang on 2016/2/23.
 */
public class MessageActivity extends BaseFragmentActivity {
    private LinearLayout llMain = null;
    private GFChatRoomGFView view = null;

    @Override
    protected int getContentView() {
        return R.layout.bjmgf_message_activity;
    }

    @Override
    protected void initView() {
        llMain = getView(R.id.bjmgf_message_chat_content_ll);
        GFChatRoomGFView.index = getIntent().getIntExtra("index", -1);
        view = new GFChatRoomGFView(this);
        llMain.addView(view);
        setTitleText(view.getTitle());
        helper.setPanelSlideListener(new GFSwipeBackActivityHelper.PanelSlideListener() {
            @Override
            public void onPanelSlide(View v, float position) {
                view.reload();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        view.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
