package com.GF.platform.uiview.messagelist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.GF.platform.uikit.util.HeaderAndFooterRecyclerViewAdapter;
import com.GF.platform.uikit.util.Util;
import com.GF.platform.uikit.widget.swipelayout.util.Attributes;
import com.GF.platform.uiview.Global;
import com.GF.platform.uiview.R;
import com.GF.platform.uiview.base.SupportLinearLayout;
import com.GF.platform.uiview.entity.Message;
import com.GF.platform.uiview.messagelist.adapter.MessageListAdapter;

/**
 * 消息列表
 * Created by sunhaoyang on 2016/6/7.
 */

public class MessgeListView extends SupportLinearLayout implements MessageListAdapter.MsgAdapterListener {

    private RecyclerView rvMessage = null;
    private LinearLayoutManager mLayoutManager = null;
    private MessageListAdapter adapter = null;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private View vFooter = null;
    private View vHeader = null;
    private RelativeLayout rlHeader = null;

    public MessgeListView(Context context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.bjmgf_message_fragment;
    }

    @Override
    protected void initView() {
        rvMessage = getView(R.id.bjmgf_message_rv);
        vHeader = View.inflate(getContext(), R.layout.bjmgf_message_list_header, null);
        vHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rlHeader = (RelativeLayout) vHeader.findViewById(R.id.bjmgf_message_head_notify_rl);
    }

    @Override
    protected void initData() {
        mLayoutManager = new LinearLayoutManager(getContext());
        rvMessage.setLayoutManager(mLayoutManager);
        adapter = new MessageListAdapter(getContext(), this);
        adapter.setMode(Attributes.Mode.Single);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvMessage.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        vFooter = new View(getContext());
        vFooter.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.gf_40dp)));
        vFooter.setBackgroundResource(R.color.gf_message_bg);
        Util.setFooterView(rvMessage, vFooter);
        Util.setHeaderView(rvMessage, vHeader);
        Global.MESSAGES.clear();
        for (int i = 0; i < 20; i++) {
            Message m = new Message();
            m.setDate("星期三");
            m.setInfo("[惬意]");
            m.setNickName("火星来客" + i);
            m.setOldPosition(i);
            Global.MESSAGES.add(m);
        }
        adapter.notifyDatasetChanged();
    }

    @Override
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
        Message msg = Global.MESSAGES.get(position);
        if (!msg.isTop()) {
            msg.setTop(true);
            Global.MESSAGES.remove(position);
            Global.MESSAGES.add(0, msg);
        } else {
            msg.setTop(false);
            Global.MESSAGES.remove(msg);
            Global.MESSAGES.add(msg.getOldPosition(), msg);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnMessageDel(int position) {
        Global.MESSAGES.remove(position);
        adapter.notifyDataSetChanged();
    }
}
