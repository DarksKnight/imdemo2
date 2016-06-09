package com.GF.platform.uiview.message;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.GF.platform.uikit.EmojiGlobal;
import com.GF.platform.uikit.Global;
import com.GF.platform.uikit.entity.Message;
import com.GF.platform.uikit.util.Util;
import com.GF.platform.uikit.widget.chatkeyboard.ChatKeyBoard;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.EmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.EmoticonPageSetEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.PageSetEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.KeyBoardListener;
import com.GF.platform.uikit.widget.chatkeyboard.util.EmojiUtil;
import com.GF.platform.uikit.widget.chatkeyboard.util.KeyBoardUtil;
import com.GF.platform.uikit.widget.chatkeyboard.util.ParseDataUtil;
import com.GF.platform.uikit.widget.dropdownlistview.DropDownListView;
import com.GF.platform.uikit.widget.tooltip.ToolTipView;
import com.GF.platform.uikit.widget.tooltip.ToolView;
import com.GF.platform.uiview.R;
import com.GF.platform.uiview.base.ViewPorts;
import com.GF.platform.uiview.message.adapter.MessageAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;

/**
 * Created by sunhaoyang on 2016/6/8.
 */

public class MessageView extends LinearLayout implements DropDownListView.OnRefreshListenerHeader, ToolView.ControlListener, KeyBoardListener, ViewPorts {

    protected Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    private ChatKeyBoard mKeyBoard = null;

    private DropDownListView mListView = null;

    private MessageAdapter adapter = null;

    //消息集合
    private List<Message> list = null;

    private Handler mHandler = null;

    private List<PageSetEntity> listEmoticons = null;

    //修改BUG：如果不在handler中操作，会导致无法正确置底
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            mListView.smoothScrollToPosition(list.size());
        }
    };

    private ToolView view = null;

    public Status currentStatus = Status.NORMAL;

    public static int index = -1;

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {

    }

    //状态（正常状态，编辑状态）
    public enum Status {
        NORMAL, EDIT
    }

    public MessageView(Context context) {
        super(context);
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

        view = new ToolView(getContext());
        ToolTipView.getInstance().make(getContext(),
                new ToolTipView.Builder(getContext()).setToolTipView(view).setListView(mListView).build());

        initKeyBoard();
    }

    private void initKeyBoard() {

        EmojiGlobal.getInstance().init(getContext());

        initEmoticons();
        mKeyBoard.setListener(this);
        mKeyBoard.getEmoticonsToolBarView()
                .addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = new File(
                                    Environment.getExternalStorageDirectory() + File.separator
                                            + "ftchat" + File.separator + "Meng.zip");
                            InputStream is = new FileInputStream(file);
                            Util.unzip(is,
                                    Environment.getExternalStorageDirectory() + File.separator
                                            + KeyBoardUtil.FOLDERNAME);
                            initEmoticons();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        mKeyBoard.getEmoticonsToolBarView().addFixedToolItemView(true, R.mipmap.icon_setting, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File file = new File(
                                    Environment.getExternalStorageDirectory() + File.separator
                                            + KeyBoardUtil.FOLDERNAME + File.separator + "Meng");
                            Util.deleteDir(file);
                            initEmoticons();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initEmoticons() {
        listEmoticons = new ArrayList<>();
        EmoticonPageSetEntity pageSetEmoji
                = new EmoticonPageSetEntity.Builder()
                .setEmoticonList(EmojiUtil.getPageSetEntity(getContext()).getEmoticonList())
                .setIPageViewInstantiateItem(mKeyBoard.getEmoticonPageViewInstantiateItem())
                .setIconId(R.mipmap.bjmgf_message_chat_emoji)
                .setLine(3)
                .setRow(7)
                .isEmoji(true)
                .build();
        listEmoticons.add(pageSetEmoji);
        File faceList = new File(
                Environment.getExternalStorageDirectory() + File.separator + KeyBoardUtil.FOLDERNAME
                        + File.separator);
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    String folderPath = folder.getAbsolutePath();
                    String name = folderPath
                            .subSequence(folderPath.lastIndexOf("/") + 1, folderPath.length()) + "";
                    EmoticonPageSetEntity pageSet
                            = new EmoticonPageSetEntity.Builder()
                            .setEmoticonList(ParseDataUtil.getPageSetEntity(name).getEmoticonList())
                            .setIPageViewInstantiateItem(
                                    mKeyBoard.getEmoticonPageViewInstantiateItem())
                            .setIconUri(Environment.getExternalStorageDirectory() + File.separator
                                    + KeyBoardUtil.FOLDERNAME + File.separator + name
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
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message m = new Message("火星海盗" + i, "撒打" + i, "11:10", "", Message.Category.NORMAL_ME,
                    false);
            list.add(m);
        }
        adapter = new MessageAdapter(getContext(), list, this, this);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListenerHead(this);
        //将消息置底
        mListView.setSelection(list.size() - 1);
        //设置触摸监听
        mListView.setOnTouchListener(getOnTouchListener());
        //设置光标处于最后
        mKeyBoard.getEditText().setSelection(mKeyBoard.getEditText().getText().length());

        //如果草稿不为空，则显示草稿
        if (Global.MESSAGES.get(index).getDraft().trim().length() > 0) {
            mKeyBoard.getEditText().setText(EmojiUtil
                    .convert(getContext(), Global.MESSAGES.get(index).getDraft(), mKeyBoard.getEditText()));
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
                ToolTipView.getInstance().remove();
                mKeyBoard.reset();
                return false;
            }
        };
    }

    @Override
    public void onRefresh() {
        final List<Message> oldMsg = new ArrayList<>();
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
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                Message m = new Message("帅的一般",
                        "帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般帅的一般般" + i, "10:22", "",
                        Message.Category.NORMAL_ME, false);
                oldMsg.add(m);
                continue;
            }
            Message m = new Message("帅的一般", "帅的一般般" + i, "10:22", "", Message.Category.NORMAL_ME,
                    false);
            oldMsg.add(m);
        }
        list.addAll(0, oldMsg);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void del(int index) {
        list.remove(list.get(index));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void more(int index) {
        currentStatus = Status.EDIT;
        mListView.enableMove(false);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setShowSelected(true);
        }
        list.get(index).setChecked(true);
        mKeyBoard.switchBoard(ChatKeyBoard.Type.DEL);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void sendMessage(String text) {
        Message msg = new Message("帅的一般", text, "22:22", "", Message.Category.NORMAL_ME, false);
        list.add(msg);
        msg = new Message("一般的帅", text, "22:23", "", Message.Category.NORMAL_YOU, false);
        list.add(msg);
        adapter.notifyDataSetChanged();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void sendEmoticon(EmoticonEntity entity) {
        Message msg = new Message("帅的一般", "", "15:15", "", Message.Category.NORMAL_ME,
                Util.getImageThumbnail(entity.getIconUri(), 200, 200), false);
        list.add(msg);
        msg = new Message("一般的帅", "", "15:20", "", Message.Category.NORMAL_YOU,
                Util.getImageThumbnail(entity.getIconUri(), 200, 200), false);
        list.add(msg);
        adapter.notifyDataSetChanged();
        mListView.requestFocus();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void delMessage() {
        currentStatus = Status.NORMAL;
        mListView.enableMove(true);

        //2016-5-10
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                list.remove(list.get(i));
                --i;
            }
            list.get(i).setShowSelected(false);
            list.get(i).setChecked(false);
        }

        adapter.notifyDataSetChanged();
        mKeyBoard.switchBoard(ChatKeyBoard.Type.NOMRAL);
    }

    @Override
    public void functionSelected(int position) {
        // 7为求包养按钮
        if (position == 7) {
            Message msg = new Message("帅的一般", "", "22:22", "", Message.Category.NURTURE, false);
            list.add(msg);
            adapter.notifyDataSetChanged();
            handler.sendEmptyMessage(0);
        }
    }

    public String getTitle() {
        return Global.MESSAGES.get(index).getNickName();
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
}
