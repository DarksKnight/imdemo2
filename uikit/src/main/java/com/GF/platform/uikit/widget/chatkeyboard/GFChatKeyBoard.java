package com.GF.platform.uikit.widget.chatkeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.GFEmojiGlobal;
import com.GF.platform.uikit.R;
import com.GF.platform.uikit.base.manager.message.GFMessageListControl;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.event.GFEventDispatch;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.audiopop.GFAudioRecordPopupWindow;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.GFChatFunctionAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.GFEmoticonsAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.GFFunctionAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.GFPageSetAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFEmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFFunction;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFPageSetEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFEmojiListener;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFKeyBoardPorts;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFPageViewInstantiateListener;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.GFEmoticonPageView;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.GFEmoticonsIndicatorView;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.GFEmoticonsToolBarView;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.GFEmoticonsViewPager;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.GFFuncLayout;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.GFSoftKeyboardSizeWatchLayout;
import com.GF.platform.uikit.widget.chatkeyboard.util.GFEmojiUtil;
import com.GF.platform.uikit.widget.chatkeyboard.util.GFKeyBoardUtil;
import com.GF.platform.uikit.widget.tooltip.GFToolTipView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 表情功能键盘
 * Created by sunhaoyang on 2016/4/19.
 */
public class GFChatKeyBoard extends GFSoftKeyboardSizeWatchLayout
        implements View.OnClickListener, GFEmoticonsToolBarView.OnToolBarItemClickListener,
        GFFuncLayout.OnFuncChangeListener, GFEmoticonsViewPager.OnEmoticonsPageViewListener,
        GFSoftKeyboardSizeWatchLayout.OnResizeListener, GFKeyBoardPorts, View.OnTouchListener {

    public static final int FUNC_TYPE_EMOTION = -1;

    public static final int FUNC_TYPE_MORE = -2;

    protected LayoutInflater mInflater = null;

    protected ImageView ivFace = null;

    protected ImageView ivMore = null;

    protected LinearLayout llKeyBoard = null;

    protected LinearLayout llKeyBoardDel = null;

    protected GFFuncLayout GFFuncLayout = null;

    protected EditText etMain = null;

    protected ImageView mBtnSend = null;

    protected GFEmoticonsViewPager GFEmoticonsViewPager = null;

    protected GFEmoticonsToolBarView GFEmoticonsToolBarView = null;

    protected GFEmoticonsIndicatorView GFEmoticonsIndicatorView = null;

    protected ViewPager vpFunction = null;

    protected LinearLayout llDot = null;

    protected Button mHoldSpeek = null;

    private List<View> views = new ArrayList<>();

    private String[] functionArray = new String[]{"照片", "拍照", "位置", "送花", "幸运骰子", "赛猪场", "名字大作战",
            "求包养", "分享游戏", "名片", "更换聊天背景"};

    protected int keyBoardHeight = (int) getResources().getDimension(R.dimen.gf_220dp);

    protected GFPageSetAdapter GFPageSetAdapter = new GFPageSetAdapter();

    private static final int ID_CHILD = R.id.id_autolayout;

    protected int mSoftKeyboardHeight = 0;

    protected int mMaxParentHeight = 0;

    private boolean isClickFunction = false;

    private int index = 0;

    private boolean isSend = false;

    private View moreView = null;

    private GFFunctionAdapter GFFunctionAdapter = null;

    private GFAudioRecordPopupWindow popupWindow = null;

    public enum Type {
        NOMRAL, DEL;
    }

    private GFEmojiListener GFEmojiListener = new GFEmojiListener() {
        @Override
        public void selectedEmoji(GFEmoticonEntity entity) {
            GFEmojiUtil.insert(etMain, GFEmojiUtil
                    .getFace(getContext(), "face/emojis/EmojiS_" + entity.getName() + ".png",
                            etMain));
        }

        @Override
        public void selectedBackSpace() {
            String content = etMain.getText().toString();
            if (content.length() > 0) {
                if (GFEmojiUtil.isDeletePng(content)) {
                    ((Editable) etMain.getText())
                            .delete(content.length() - GFEmojiUtil.emojiFormat.length(),
                                    content.length());
                } else {
                    ((Editable) etMain.getText()).delete(content.length() - 1, content.length());
                }
            }
        }
    };

    public GFChatKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflateKeyboardBar();
        initView();
        initFuncView();
        initEditText();
        addOnResizeListener(this);
    }

    protected void inflateKeyboardBar() {
        mInflater.inflate(R.layout.bjmgf_message_chat_keyboard, this);
    }

    protected View inflateFunc() {
        return mInflater.inflate(R.layout.bjmgf_message_chat_view_func, null);
    }

    protected View inflateMore() {
        return mInflater.inflate(R.layout.bjmgf_message_chat_view_more, null);
    }

    protected void initView() {
        llKeyBoard = getView(R.id.bjmgf_message_keyboard);
        llKeyBoardDel = getView(R.id.bjmgf_message_keyboard_del);
        ivFace = getView(R.id.bjmgf_message_chat_btn_face);
        ivMore = getView(R.id.bjmgf_message_chat_btn_more);
        GFFuncLayout = getView(R.id.bjmgf_message_chat_fl);
        etMain = getView(R.id.bjmgf_message_chat_et);
        mBtnSend = getView(R.id.bjmgf_message_chat_sound_iv);
        mHoldSpeek = getView(R.id.bjmgf_message_chat_hold_tv);

        ivFace.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        llKeyBoardDel.setOnClickListener(this);
    }

    protected void initFuncView() {
        View keyboardView = inflateFunc();
        GFFuncLayout.addFuncView(FUNC_TYPE_EMOTION, keyboardView);
        GFEmoticonsViewPager = getView(R.id.bjmgf_message_chat_view_epv);
        GFEmoticonsIndicatorView = getView(R.id.bjmgf_message_view_eiv);
        GFEmoticonsToolBarView = getView(R.id.bjmgf_message_chat_view_etv);
        GFEmoticonsToolBarView.setOnToolBarItemClickListener(this);
        GFFuncLayout.setOnFuncChangeListener(this);
        GFEmoticonsViewPager.setOnIndicatorListener(this);
        mHoldSpeek.setOnTouchListener(this);

        moreView = inflateMore();
        GFFuncLayout.addFuncView(FUNC_TYPE_MORE, moreView);
        vpFunction = getView(R.id.bjmgf_message_chat_function_vp);
        llDot = getView(R.id.bjmgf_message_chat_function_dots);
        initMoreView();
    }

    private void initMoreView() {
        List<GFFunction> listGFFunction = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            GFFunction f = null;
            try {
                f = new GFFunction(functionArray[i], BitmapFactory.decodeStream(
                        getContext().getAssets().open("function/function_" + i + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            listGFFunction.add(f);
        }
        for (int i = 0; i < 2; i++) {
            views.add(viewPagerItem(i, listGFFunction));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(16, 16);
            llDot.addView(dotsItem(i), params);
        }
        GFChatFunctionAdapter adapter = new GFChatFunctionAdapter(views);
        vpFunction.setAdapter(adapter);
        llDot.getChildAt(0).setSelected(true);
        vpFunction.addOnPageChangeListener(new PageChange());
    }

    /**
     * KeyBoardListener
     * 获取viewpager item
     */
    private View viewPagerItem(int position, final List<GFFunction> list) {
        int columns = 4;
        int rows = 2;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_chat_function_gridview, null);
        GridView gv = (GridView) view.findViewById(R.id.bjmgf_message_chat_function_gv);
        List<GFFunction> subList = new ArrayList<>();
        subList.addAll(list
                .subList(position * (columns * rows),
                        (columns * rows) * (position + 1) > list
                                .size() ? list.size() : (columns
                                * rows)
                                * (position + 1)));
        GFFunctionAdapter = new GFFunctionAdapter(getContext(), subList,
                new GFFunctionAdapter.Listener() {
                    @Override
                    public void functionSelected(int position) {
                        GFEventDispatch.post(GFConstant.EVENT_FUNCTION_SELECTED, position);
                    }
                });
        gv.setAdapter(GFFunctionAdapter);
        gv.setNumColumns(columns);
        return gv;
    }

    /**
     * 圆点item
     */
    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.bjmgf_message_chat_function_dots, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.bjmgf_message_chat_function_dot);
        iv.setId(position);
        return iv;
    }

    protected void initEditText() {
        etMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!etMain.isFocused()) {
                    etMain.setFocusable(true);
                    etMain.setFocusableInTouchMode(true);
                    isClickFunction = false;
                }
                return false;
            }
        });
        //edittext增加文本改变监听
        etMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //每次文本变化，都放入草稿中
                GFMessageListControl.getDefault().getGFMessages().get(index).setDraft(s.toString());
                if (count > 0) {
                    isSend = true;
                    mBtnSend.setImageResource(R.mipmap.bjmgf_message_chat_send_btn);
                    int length = s.toString().length();
                    if (length > 0) {
                        String sign = s.toString().substring(start, length);
                        String emojiId = GFEmojiGlobal.getInstance().getEmojisCode().get(sign);
                        if (emojiId != null) {
                            ((Editable) etMain.getText()).delete(start, length);
                            GFEmojiUtil.insert(etMain, GFEmojiUtil
                                    .getFace(getContext(), "face/emojis/EmojiS_" + emojiId + ".png",
                                            etMain));
                        }
                    }
                } else {
                    isSend = false;
                    mBtnSend.setImageResource(R.mipmap.bjmgf_message_chat_sound_btn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    protected void setAdapter(GFPageSetAdapter GFPageSetAdapter) {
        if (null != GFPageSetAdapter) {
            ArrayList<GFPageSetEntity> pageSetEntities = GFPageSetAdapter.getPageSetEntityList();
            GFEmoticonsToolBarView.reset();
            if (null != pageSetEntities) {
                for (GFPageSetEntity GFPageSetEntity : pageSetEntities) {
                    GFEmoticonsToolBarView.addToolItemView(GFPageSetEntity);
                }
            }
        }
        GFEmoticonsViewPager.setAdapter(GFPageSetAdapter);
    }

    public void addEmoticons(List<GFPageSetEntity> GFPageSetEntityList) {
        GFPageSetAdapter.getPageSetEntityList().clear();
        for (GFPageSetEntity p : GFPageSetEntityList) {
            GFPageSetAdapter.add(p);
        }
        setAdapter(GFPageSetAdapter);
    }

    @Override
    public void onClick(View v) {
        GFToolTipView.getInstance().remove();
        int id = v.getId();
        if (id == R.id.bjmgf_message_chat_btn_face) {
            isClickFunction = true;
            GFFuncLayout.updateHeight(keyBoardHeight);
            toggleFuncView(FUNC_TYPE_EMOTION);
            hideSpeek();
        } else if (id == R.id.bjmgf_message_chat_btn_more) {
            isClickFunction = true;
            GFFuncLayout.updateHeight(keyBoardHeight);
            toggleFuncView(FUNC_TYPE_MORE);
            hideSpeek();
        } else if (id == R.id.bjmgf_message_chat_sound_iv) {
            if (isSend) {
                //test
                GFMessage gfMessage = new GFMessage("帅的一般", etMain.getText().toString(), GFUtil.getDate(), "", GFMessage.Category.NORMAL_ME, false);
                GFEventDispatch.post(GFConstant.EVENT_SEND_MESSAGE, gfMessage);
                etMain.setText("");
            } else {
                if (etMain.isEnabled()) {
                    reset();
                    showSpeek();
                } else {
                    GFKeyBoardUtil.openSoftKeyboard(etMain);
                    hideSpeek();
                }
            }
        } else if (id == R.id.bjmgf_message_keyboard_del) {
            GFEventDispatch.post(GFConstant.EVENT_DELETE_MESSAGE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.bjmgf_message_chat_hold_tv) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ((Activity) getContext()).getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow = new GFAudioRecordPopupWindow(getContext(), llKeyBoard.getMeasuredHeight());
                        holdAudioRecord();
                    }
                });
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                moveAudioRecord(event.getRawX(), event.getRawY());
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (popupWindow.getStatus() == GFConstant.AUDIO_STATUS_DEFAULT) {
                    GFMessage GFMessage = upAudioRecord();
                    GFEventDispatch.post(GFConstant.EVENT_SEND_MESSAGE, GFMessage);
                } else if (popupWindow.getStatus() == GFConstant.AUDIO_STATUS_PLAY) {
                    mHoldSpeek.setText(getResources().getText(R.string.bjmgf_message_chat_hold_speek));
                    popupWindow.showReplay();
                } else if (popupWindow.getStatus() == GFConstant.AUDIO_STATUS_CANCEL) {
                    upAudioRecord();
                }
            }
        }
        return false;
    }

    private void hideSpeek() {
        mHoldSpeek.setVisibility(View.GONE);
        etMain.setEnabled(true);
        mBtnSend.setImageResource(R.mipmap.bjmgf_message_chat_sound_btn);
    }

    private void showSpeek() {
        mHoldSpeek.setVisibility(View.VISIBLE);
        etMain.setEnabled(false);
        mBtnSend.setImageResource(R.mipmap.bjmgf_message_chat_keyboard);
    }

    protected void toggleFuncView(int key) {
        GFFuncLayout.toggleFuncView(key, isSoftKeyboardPop(), etMain);
    }

    @Override
    public void onFuncChange(int key) {

    }

    @Override
    public void onToolBarItemClick(GFPageSetEntity GFPageSetEntity) {
        GFEmoticonsViewPager.setCurrentPageSet(GFPageSetEntity);
    }

    @Override
    public void emoticonSetChanged(GFPageSetEntity GFPageSetEntity) {
        GFEmoticonsToolBarView.setToolBtnSelect(GFPageSetEntity.getUuid());
    }

    @Override
    public void playTo(int position, GFPageSetEntity GFPageSetEntity) {
        GFEmoticonsIndicatorView.playTo(position, GFPageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, GFPageSetEntity GFPageSetEntity) {
        GFEmoticonsIndicatorView.playBy(oldPosition, newPosition, GFPageSetEntity);
    }

    @Override
    public void OnSoftPop(int height) {
        if (mSoftKeyboardHeight != height) {
            mSoftKeyboardHeight = height;
            GFKeyBoardUtil.setDefKeyboardHeight(getContext(), mSoftKeyboardHeight);
        }
        GFFuncLayout.updateHeight(height);
        GFFuncLayout.setVisibility(true);
    }

    @Override
    public void OnSoftClose() {
        if (!isClickFunction) {
            reset();
        } else {
            isClickFunction = false;
        }
    }

    @Override
    public void reset() {
        GFKeyBoardUtil.closeSoftKeyboard(this);
        GFFuncLayout.hideAllFuncView();
        hideFunctionLayout();
    }

    public void hideFunctionLayout() {
        moreView.setVisibility(View.GONE);
    }

    @Override
    public void switchBoard(Type type) {
        if (type == Type.DEL) {
            llKeyBoard.setVisibility(View.GONE);
            llKeyBoardDel.setVisibility(View.VISIBLE);
        } else if (type == Type.NOMRAL) {
            llKeyBoard.setVisibility(View.VISIBLE);
            llKeyBoardDel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxParentHeight != 0) {
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childSum = getChildCount();
        if (childSum > 1) {
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child, index, params);
        if (childSum == 0) {
            if (child.getId() < 0) {
                child.setId(ID_CHILD);
            }
            RelativeLayout.LayoutParams paramsChild = (RelativeLayout.LayoutParams) child
                    .getLayoutParams();
            paramsChild.addRule(ALIGN_PARENT_BOTTOM);
            child.setLayoutParams(paramsChild);
        } else if (childSum == 1) {
            RelativeLayout.LayoutParams paramsChild = (RelativeLayout.LayoutParams) child
                    .getLayoutParams();
            paramsChild.addRule(ABOVE, ID_CHILD);
            child.setLayoutParams(paramsChild);
        }
    }

    public GFPageViewInstantiateListener<GFPageEntity> getEmoticonPageViewInstantiateItem() {
        return new GFPageViewInstantiateListener<GFPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, GFPageEntity GFPageEntity) {
                if (GFPageEntity.getRootView() == null) {
                    GFEmoticonPageView pageView = new GFEmoticonPageView(container.getContext());
                    pageView.setNumColumns(GFPageEntity.getRow());
                    GFPageEntity.setRootView(pageView);
                    try {
                        GFEmoticonsAdapter adapter = new GFEmoticonsAdapter(container.getContext(),
                                GFPageEntity.getEmoticonList(), GFEmojiListener);
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return GFPageEntity.getRootView();
            }
        };
    }

    private class PageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < llDot.getChildCount(); i++) {
                llDot.getChildAt(i).setSelected(false);
            }
            llDot.getChildAt(arg0).setSelected(true);
        }
    }

    public void setMessageIndex(int index) {
        this.index = index;
    }

    public EditText getEditText() {
        return etMain;
    }

    public GFEmoticonsToolBarView getGFEmoticonsToolBarView() {
        return GFEmoticonsToolBarView;
    }

    public void holdAudioRecord() {
        popupWindow.showAtLocation();
        mHoldSpeek.setText(getResources().getText(R.string.bjmgf_message_chat_up_speek));
        ((Activity) getContext()).getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                popupWindow.initViewLocation();
            }
        });
    }

    public void moveAudioRecord(float x, float y) {
        popupWindow.setPointLocation(x, y);
    }

    public GFMessage upAudioRecord() {
        mHoldSpeek.setText(getResources().getText(R.string.bjmgf_message_chat_hold_speek));
        popupWindow.reset();
        popupWindow.dismiss();
        return popupWindow.getMessage();
    }
}
