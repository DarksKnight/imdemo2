package com.GF.platform.gfplatform.ui.fragment.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.GF.platform.gfplatform.R;
import com.GF.platform.gfplatform.base.BaseFragment;
import com.GF.platform.gfplatform.base.Global;
import com.GF.platform.gfplatform.base.ListCallBack;
import com.GF.platform.gfplatform.entity.Message;
import com.GF.platform.gfplatform.model.message.MessageFragementModel;
import com.GF.platform.gfplatform.model.message.impl.MessageFragmentModelImpl;
import com.GF.platform.gfplatform.ui.fragment.message.adapter.MessageAdapter;
import com.GF.platform.uikit.util.HeaderAndFooterRecyclerViewAdapter;
import com.GF.platform.uikit.util.Util;
import com.GF.platform.uikit.widget.swipelayout.util.Attributes;

import java.util.List;

/**
 * 消息列表
 * Created by sunhaoyang on 2016/2/19.
 */
public class MessageFragment extends BaseFragment implements MessageAdapter.MsgAdapterListener {

    private RecyclerView rvMessage = null;
    private LinearLayoutManager mLayoutManager = null;
    private MessageAdapter adapter = null;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private View vFooter = null;
    private View vHeader = null;
    private RelativeLayout rlHeader = null;

    @Override
    protected int getContentView() {
        return R.layout.bjmgf_message_fragment;
    }

    @Override
    protected void initView() {
        rvMessage = getView(R.id.bjmgf_message_rv);
        vHeader = View.inflate(getActivity(), R.layout.bjmgf_message_list_header, null);
        vHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rlHeader = (RelativeLayout) vHeader.findViewById(R.id.bjmgf_message_head_notify_rl);
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
    protected void initData() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMessage.setLayoutManager(mLayoutManager);
        adapter = new MessageAdapter(this, this);
        adapter.setMode(Attributes.Mode.Single);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvMessage.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        vFooter = new View(getActivity());
        vFooter.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.gf_40dp)));
        vFooter.setBackgroundResource(R.color.gf_message_bg);
        Util.setFooterView(rvMessage, vFooter);
        Util.setHeaderView(rvMessage, vHeader);
        MessageFragementModel model = new MessageFragmentModelImpl();
        dealProxy.dealView(new ListCallBack<Message>() {
            @Override
            public void getData(List<Message> listData) {
                adapter.notifyDatasetChanged();
            }

            @Override
            public void getError(String error) {

            }
        }, model);
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
