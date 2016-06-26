package com.GF.platform.uiview.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.GFEmojiGlobal;
import com.GF.platform.uikit.base.GFViewBasePorts;
import com.GF.platform.uikit.base.manager.message.GFMessageControl;
import com.GF.platform.uikit.base.manager.message.GFMessageListControl;
import com.GF.platform.uikit.base.manager.message.GFMessageManager;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.event.GFDeleteMessageEvent;
import com.GF.platform.uikit.event.GFEventDispatch;
import com.GF.platform.uikit.event.GFFunctionSelectedEvent;
import com.GF.platform.uikit.event.GFImageSelectEvent;
import com.GF.platform.uikit.event.GFSendMessageEvent;
import com.GF.platform.uikit.util.GFLogProxy;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.chatkeyboard.GFChatKeyBoard;
import com.GF.platform.uikit.widget.chatkeyboard.base.command.GFCommandPerformer;
import com.GF.platform.uikit.widget.chatkeyboard.base.command.GFNurtureCommand;
import com.GF.platform.uikit.widget.chatkeyboard.base.command.GFSelectPicCommand;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFEmoticonPageSetEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageSetEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFKeyBoardCommand;
import com.GF.platform.uikit.widget.chatkeyboard.util.GFEmojiUtil;
import com.GF.platform.uikit.widget.chatkeyboard.util.GFKeyBoardUtil;
import com.GF.platform.uikit.widget.chatkeyboard.util.GFParseDataUtil;
import com.GF.platform.uikit.widget.dropdownlistview.GFDropDownListView;
import com.GF.platform.uikit.widget.multiselector.GFMultiImageSelectorActivity;
import com.GF.platform.uikit.widget.tooltip.GFToolTipView;
import com.GF.platform.uikit.widget.tooltip.GFToolView;
import com.GF.platform.uiview.R;
import com.GF.platform.uiview.base.GFViewPorts;
import com.GF.platform.uiview.message.adapter.GFChatRoomAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;

/**
 * 消息详情
 * Created by sunhaoyang on 2016/6/8.
 */

public class GFChatRoomView extends LinearLayout implements GFDropDownListView.OnRefreshListenerHeader, GFToolView.ControlListener, GFViewPorts, GFViewBasePorts {

    protected Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    private GFChatKeyBoard mKeyBoard = null;

    private GFDropDownListView mListView = null;

    private GFChatRoomAdapter adapter = null;

    private Handler mHandler = null;

    private List<GFPageSetEntity> listEmoticons = null;

    private List<GFMessage> totalOldMsg = new ArrayList<>();

    private GFMessageControl mGFMessageControl = new GFMessageControl();

    //修改BUG：如果不在handler中操作，会导致无法正确置底
    private Handler handler = new Handler(getContext().getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            mListView.smoothScrollToPosition(mGFMessageControl.getMessageSize());
        }
    };

    private GFToolView view = null;

    public Status currentStatus = Status.NORMAL;

    public static int index = -1;

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void finish() {
        mKeyBoard.reset();
        mGFMessageControl.remove(totalOldMsg);
        GFEventDispatch.unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> selectPath = data.getStringArrayListExtra(GFMultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : selectPath) {
                    sb.append(p);
                    sb.append("\n");
                }
                Log.i("BJMEngine", "path = " + sb.toString());
            }
        }
    }

    @Override
    public void uiRefresh() {
        adapter.notifyDataSetChanged();
        handler.sendEmptyMessage(0);
    }

    //状态（正常状态，编辑状态）
    public enum Status {
        NORMAL, EDIT
    }

    public GFChatRoomView(Context context) {
        super(context);
        GFEventDispatch.register(this);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_view, this, false);
        LayoutParams defaultLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, defaultLayoutParam);
        initView();
        initData();
    }

    protected void initView() {
        mKeyBoard = getView(R.id.bjmgf_message_chat_keyboard);
        mListView = getView(R.id.bjmgf_message_chat_listview);

        view = new GFToolView(getContext());
        //长按出现复制等功能的视图
        GFToolTipView.getInstance().make(new GFToolTipView.Builder(getContext()).setToolTipView(view).setListView(mListView).build());

        initKeyBoard();
    }

    private void initKeyBoard() {
        GFEmojiGlobal.getInstance().init(getContext());
        initEmoticons();
        //test
        mKeyBoard.getGFEmoticonsToolBarView()
                .addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = new File(
                                    Environment.getExternalStorageDirectory() + File.separator
                                            + "ftchat" + File.separator + "Meng.zip");
                            InputStream is = new FileInputStream(file);
                            GFUtil.unzip(is,
                                    Environment.getExternalStorageDirectory() + File.separator
                                            + GFKeyBoardUtil.FOLDERNAME);
                            initEmoticons();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        mKeyBoard.getGFEmoticonsToolBarView().addFixedToolItemView(true, R.mipmap.icon_setting, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = new File(
                                    Environment.getExternalStorageDirectory() + File.separator
                                            + GFKeyBoardUtil.FOLDERNAME + File.separator + "Meng");
                            GFUtil.deleteDir(file);
                            initEmoticons();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initEmoticons() {
        listEmoticons = new ArrayList<>();
        GFEmoticonPageSetEntity pageSetEmoji
                = new GFEmoticonPageSetEntity.Builder()
                .setEmoticonList(GFEmojiUtil.getPageSetEntity(getContext()).getEmoticonList())
                .setIPageViewInstantiateItem(mKeyBoard.getEmoticonPageViewInstantiateItem())
                .setIconId(R.mipmap.bjmgf_message_chat_emoji)
                .setLine(3)
                .setRow(7)
                .isEmoji(true)
                .build();
        listEmoticons.add(pageSetEmoji);
        File faceList = new File(
                Environment.getExternalStorageDirectory() + File.separator + GFKeyBoardUtil.FOLDERNAME
                        + File.separator);
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    String folderPath = folder.getAbsolutePath();
                    String name = folderPath
                            .subSequence(folderPath.lastIndexOf("/") + 1, folderPath.length()) + "";
                    GFEmoticonPageSetEntity pageSet
                            = new GFEmoticonPageSetEntity.Builder()
                            .setEmoticonList(GFParseDataUtil.getPageSetEntity(name).getEmoticonList())
                            .setIPageViewInstantiateItem(
                                    mKeyBoard.getEmoticonPageViewInstantiateItem())
                            .setIconUri(Environment.getExternalStorageDirectory() + File.separator
                                    + GFKeyBoardUtil.FOLDERNAME + File.separator + name
                                    + File.separator + "chat_" + name.toLowerCase() + "_icon"
                                    + ".png")
                            .build();
                    listEmoticons.add(pageSet);
                }
            }
        }
        mKeyBoard.addEmoticons(listEmoticons);
    }

    protected void initData() {
        //获取当前消息的索引
        mKeyBoard.setMessageIndex(index);
        //test
        if (GFMessageManager.getDefault().getMessageControl(index + "") == null) {
            for (int i = 0; i < 10; i++) {
                GFMessage m = new GFMessage("火星海盗" + i, "撒打" + i, "11:10", "", GFMessage.Category.NORMAL_ME,
                        false);
                m.setMsgId(System.currentTimeMillis() / 1000 + "");
                mGFMessageControl.addMessage(m);
            }
            GFMessageManager.getDefault().put(index + "", mGFMessageControl);
        }
        mGFMessageControl = GFMessageManager.getDefault().getMessageControl(index + "");

        adapter = new GFChatRoomAdapter(getContext(), mGFMessageControl, this, this, mKeyBoard);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListenerHead(this);
        //将消息置底
        mListView.setSelection(mGFMessageControl.getMessageSize() - 1);
        //设置触摸监听
        mListView.setOnTouchListener(getOnTouchListener());
        //设置光标处于最后
        mKeyBoard.getEditText().setSelection(mKeyBoard.getEditText().getText().length());

        GFMessage GFMessage = GFMessageListControl.getDefault().getMessage(index);
        //如果草稿不为空，则显示草稿
        if (GFMessage.getDraft().trim().length() > 0) {
            mKeyBoard.getEditText().setText(GFEmojiUtil
                    .convert(getContext(), GFMessage.getDraft(), mKeyBoard.getEditText()));
        }
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GFToolTipView.getInstance().remove();
                mKeyBoard.reset();
                return false;
            }
        };
    }

    @Override
    public void onRefresh() {
        final List<GFMessage> oldMsg = new ArrayList<>();
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                adapter.notifyDataSetChanged();
                if (!mListView.isMove()) {
                    mListView.setSelectionFromTop(oldMsg.size() + 1,
                            (int) getResources().getDimension(R.dimen.gf_40dp));
                }
                mListView.onRefreshCompleteHeader();
            }
        };
        //test
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                GFMessage m = new GFMessage("帅的一般",
                        "帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般" + i, "10:22", "",
                        GFMessage.Category.NORMAL_ME, false);
                oldMsg.add(m);
                continue;
            }
            GFMessage m = new GFMessage("帅的一般", "帅的一般般" + i, "10:22", "", GFMessage.Category.NORMAL_ME,
                    false);
            oldMsg.add(m);
        }
        mGFMessageControl.addAll(0, oldMsg);
        totalOldMsg.addAll(oldMsg);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void del(GFMessage GFMessage) {
        mGFMessageControl.remove(GFMessage);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void more(GFMessage GFMessage) {
        currentStatus = Status.EDIT;
        mListView.enableMove(false);
        for (int i = 0; i < mGFMessageControl.getMessageSize(); i++) {
            mGFMessageControl.getMessage(i).setShowSelected(true);
        }
        GFMessage.setChecked(true);
        mKeyBoard.switchBoard(GFChatKeyBoard.Type.DEL);
        adapter.notifyDataSetChanged();
    }

    public String getTitle() {
        return GFMessageListControl.getDefault().getGFMessages().get(index).getNickName();
    }

    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();

        //by lituo
        //  react-native do not use android's layout system,
        //  do measure and layout here.
        post(measureAndLayout);
    }

    /**
     * 收到发送消息事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendMessageEvent(GFSendMessageEvent event) {
        //test
        if (event.gfMessage.getAudioTime() == 0) {
            mGFMessageControl.addMessage(event.gfMessage);
//            GFMessage msg = new GFMessage("一般的帅", event.GFMessage.getInfo(), GFUtil.getDate(), "", GFMessage.Category.NORMAL_YOU, event.GFMessage.getExpression(), false);
//            msg.setSending(true);
//            mGFMessageControl.addMessage(msg);
        } else {
            mGFMessageControl.addMessage(event.gfMessage);
            GFMessage gfMessage = new GFMessage("一般的帅", "", GFUtil.getDate(), "", GFMessage.Category.NORMAL_YOU, null, false);
            gfMessage.setAudioTime(5000);
            mGFMessageControl.addMessage(gfMessage);
        }
        uiRefresh();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mGFMessageControl.getMessage(mGFMessageControl.getMessageSize() - 1).setSending(false);
                uiRefresh();
            }
        }, 2000);
    }

    /**
     * 收到删除消息事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteMessageEvent(GFDeleteMessageEvent event) {
        currentStatus = Status.NORMAL;
        mListView.enableMove(true);

        //2016-5-10
        for (int i = 0; i < mGFMessageControl.getMessageSize(); i++) {
            if (mGFMessageControl.getMessage(i).isChecked()) {
                mGFMessageControl.remove(mGFMessageControl.getMessage(i));
                --i;
            }
            mGFMessageControl.getMessage(i).setShowSelected(false);
            mGFMessageControl.getMessage(i).setChecked(false);
        }

        adapter.notifyDataSetChanged();
        mKeyBoard.switchBoard(GFChatKeyBoard.Type.NOMRAL);
    }

    /**
     * 收到功能选择事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFunctionSelectedEvent(GFFunctionSelectedEvent event) {
        GFCommandPerformer cp = GFCommandPerformer.getDefault();
        GFKeyBoardCommand c = null;
        if (event.str.equals("照片")) {
            c = new GFSelectPicCommand(getContext());
        } else if (event.str.equals("求包养")) {
            c = new GFNurtureCommand(mGFMessageControl, this);
        }
        cp.setCommand(c);
        cp.doCommand();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageSelectEvent(GFImageSelectEvent event) {
        for (String str : event.resultList) {
            GFMessage gfMessage = new GFMessage("一般的帅", "", GFUtil.getDate(), "", GFMessage.Category.NORMAL_ME, null, false);
            gfMessage.setPicture(str);
            GFEventDispatch.post(GFConstant.EVENT_SEND_MESSAGE, gfMessage);
        }
    }

    public void reload() {
        mKeyBoard.reset();
    }
}
