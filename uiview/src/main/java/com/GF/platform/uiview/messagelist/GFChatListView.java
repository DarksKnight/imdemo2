package com.GF.platform.uiview.messagelist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.GF.platform.uikit.base.manager.message.GFMessageListControl;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.util.GFHeaderAndFooterRecyclerViewAdapter;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.swipelayout.util.GFAttributes;
import com.GF.platform.uiview.R;
import com.GF.platform.uiview.base.GFViewPorts;
import com.GF.platform.uiview.messagelist.adapter.GFChatListAdapter;

/**
 * 消息列表
 * Created by sunhaoyang on 2016/6/7.
 */

public class GFChatListView extends LinearLayout implements GFChatListAdapter.MsgAdapterListener, GFViewPorts {

    private RecyclerView rvMessage = null;
    private LinearLayoutManager mLayoutManager = null;
    private GFChatListAdapter adapter = null;
    private GFHeaderAndFooterRecyclerViewAdapter mGFHeaderAndFooterRecyclerViewAdapter = null;
    private View vFooter = null;
    private View vHeader = null;
    private RelativeLayout rlHeader = null;

    public GFChatListView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_list_view, this, false);
        LayoutParams defaultLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, defaultLayoutParam);
        initView();
        initData();
        initListener();
    }

    protected void initView() {
        rvMessage = getView(R.id.bjmgf_message_rv);
        vHeader = View.inflate(getContext(), R.layout.bjmgf_message_list_header, null);
        vHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rlHeader = (RelativeLayout) vHeader.findViewById(R.id.bjmgf_message_head_notify_rl);
    }

    protected void initData() {
        mLayoutManager = new LinearLayoutManager(getContext());
        rvMessage.setLayoutManager(mLayoutManager);
        adapter = new GFChatListAdapter(getContext(), this);
        adapter.setMode(GFAttributes.Mode.Single);
        mGFHeaderAndFooterRecyclerViewAdapter = new GFHeaderAndFooterRecyclerViewAdapter(adapter);
        rvMessage.setAdapter(mGFHeaderAndFooterRecyclerViewAdapter);

        vFooter = new View(getContext());
        vFooter.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.gf_40dp)));
        vFooter.setBackgroundResource(R.color.gf_message_bg);
        GFUtil.setFooterView(rvMessage, vFooter);
        GFUtil.setHeaderView(rvMessage, vHeader);
        GFMessageListControl.getDefault().clear();
        //test
        for (int i = 0; i < 20; i++) {
            GFMessage m = new GFMessage();
            m.setDate("星期三");
            m.setInfo("[惬意]");
            m.setNickName("火星来客" + i);
            m.setOldPosition(i);
            GFMessageListControl.getDefault().addMessage(m);
        }
        adapter.notifyDatasetChanged();
    }

    protected void initListener() {
        vHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rlHeader.setBackgroundColor(getResources().getColor(R.color.gf_message_swipe_action_down));
                        break;
                    case MotionEvent.ACTION_UP:
                        rlHeader.setBackgroundColor(getResources().getColor(R.color.gf_white));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        rlHeader.setBackgroundColor(getResources().getColor(R.color.gf_white));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void OnMessageTop(int position) {
        GFMessage msg = GFMessageListControl.getDefault().getMessage(position);
        if (!msg.isTop()) {
            msg.setTop(true);
            GFMessageListControl.getDefault().remove(position);
            GFMessageListControl.getDefault().addMessage(0, msg);
        } else {
            msg.setTop(false);
            GFMessageListControl.getDefault().remove(msg);
            GFMessageListControl.getDefault().addMessage(msg.getOldPosition(), msg);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnMessageDel(int position) {
        GFMessageListControl.getDefault().remove(position);
        adapter.notifyDataSetChanged();
    }

    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
