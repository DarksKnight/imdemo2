package com.GF.platform.uiview.messagelist;

import com.GF.platform.uikit.base.manager.message.MessageListControl;
import com.GF.platform.uikit.entity.Message;
import com.GF.platform.uikit.util.HeaderAndFooterRecyclerViewAdapter;
import com.GF.platform.uikit.util.Util;
import com.GF.platform.uikit.widget.swipelayout.util.Attributes;
import com.GF.platform.uiview.R;
import com.GF.platform.uiview.base.ViewPorts;
import com.GF.platform.uiview.messagelist.adapter.MessageListAdapter;
import com.GF.platform.uiview.service.MessageService;

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

/**
 * 消息列表
 * Created by sunhaoyang on 2016/6/7.
 */

public class MessageListView extends LinearLayout implements MessageListAdapter.MsgAdapterListener, ViewPorts {

    private RecyclerView rvMessage = null;
    private LinearLayoutManager mLayoutManager = null;
    private MessageListAdapter adapter = null;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private View vFooter = null;
    private View vHeader = null;
    private RelativeLayout rlHeader = null;

    public MessageListView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_list_view, this, false);
        LayoutParams defaultLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, defaultLayoutParam);
        initView();
        initData();
        initListener();
        openMessageService();
    }

    private void openMessageService() {
        Intent messageService = new Intent(getContext(), MessageService.class);
        getContext().startService(messageService);
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
        adapter = new MessageListAdapter(getContext(), this);
        adapter.setMode(Attributes.Mode.Single);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvMessage.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        vFooter = new View(getContext());
        vFooter.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.gf_40dp)));
        vFooter.setBackgroundResource(R.color.gf_message_bg);
        Util.setFooterView(rvMessage, vFooter);
        Util.setHeaderView(rvMessage, vHeader);
        MessageListControl.getInstance().clear();
        for (int i = 0; i < 20; i++) {
            Message m = new Message();
            m.setDate("星期三");
            m.setInfo("[惬意]");
            m.setNickName("火星来客" + i);
            m.setOldPosition(i);
            MessageListControl.getInstance().addMessage(m);
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
        Message msg = MessageListControl.getInstance().getMessage(position);
        if (!msg.isTop()) {
            msg.setTop(true);
            MessageListControl.getInstance().remove(position);
            MessageListControl.getInstance().addMessage(0, msg);
        } else {
            msg.setTop(false);
            MessageListControl.getInstance().remove(msg);
            MessageListControl.getInstance().addMessage(msg.getOldPosition(), msg);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnMessageDel(int position) {
        MessageListControl.getInstance().remove(position);
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
}
