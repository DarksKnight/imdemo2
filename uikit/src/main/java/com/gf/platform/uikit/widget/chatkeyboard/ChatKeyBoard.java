package com.GF.platform.uikit.widget.chatkeyboard;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.GF.platform.uikit.EmojiGlobal;
import com.GF.platform.uikit.Global;
import com.GF.platform.uikit.R;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.ChatFunctionAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.EmoticonsAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.FunctionAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.adapter.PageSetAdapter;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.EmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.Function;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.PageEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.PageSetEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.EmojiListener;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.KeyBoardListener;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.KeyBoardPorts;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.PageViewInstantiateListener;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.EmoticonPageView;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.EmoticonsIndicatorView;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.EmoticonsToolBarView;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.EmoticonsViewPager;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.FuncLayout;
import com.GF.platform.uikit.widget.chatkeyboard.base.widget.SoftKeyboardSizeWatchLayout;
import com.GF.platform.uikit.widget.chatkeyboard.util.EmojiUtil;
import com.GF.platform.uikit.widget.chatkeyboard.util.KeyBoardUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 表情功能键盘
 * Created by sunhaoyang on 2016/4/19.
 */
public class ChatKeyBoard extends SoftKeyboardSizeWatchLayout
        implements View.OnClickListener, EmoticonsToolBarView.OnToolBarItemClickListener,
        FuncLayout.OnFuncChangeListener, EmoticonsViewPager.OnEmoticonsPageViewListener,
        SoftKeyboardSizeWatchLayout.OnResizeListener, KeyBoardPorts {

    public static final int FUNC_TYPE_EMOTION = -1;

    public static final int FUNC_TYPE_MORE = -2;

    protected LayoutInflater mInflater = null;

    protected ImageView ivFace = null;

    protected ImageView ivMore = null;

    protected LinearLayout llKeyBoard = null;

    protected LinearLayout llKeyBoardDel = null;

    protected FuncLayout funcLayout = null;

    protected EditText etMain = null;

    protected ImageView mBtnSend = null;

    protected EmoticonsViewPager emoticonsViewPager = null;

    protected EmoticonsToolBarView emoticonsToolBarView = null;

    protected EmoticonsIndicatorView emoticonsIndicatorView = null;

    protected ViewPager vpFunction = null;

    protected LinearLayout llDot = null;

    private List<View> views = new ArrayList<>();

    private String[] functionArray = new String[]{"照片", "拍照", "位置", "送花", "幸运骰子", "赛猪场", "名字大作战",
            "求包养", "分享游戏", "名片", "更换聊天背景"};

    protected int keyBoardHeight = (int) getResources().getDimension(R.dimen.gf_220dp);

    protected PageSetAdapter pageSetAdapter = new PageSetAdapter();

    private static final int ID_CHILD = R.id.id_autolayout;

    protected int mSoftKeyboardHeight = 0;

    protected int mMaxParentHeight = 0;

    private boolean isClickFunction = false;

    private int index = 0;

    private boolean isSend = false;

    private KeyBoardListener listener = null;

    private View moreView = null;

    private FunctionAdapter functionAdapter = null;

    public enum Type {
        NOMRAL, DEL;
    }

    private EmojiListener emojiListener = new EmojiListener() {
        @Override
        public void selectedEmoji(EmoticonEntity entity) {
            EmojiUtil.insert(etMain, EmojiUtil
                    .getFace(getContext(), "face/emojis/EmojiS_" + entity.getName() + ".png",
                            etMain));
        }

        @Override
        public void selectedBackSpace() {
            String content = etMain.getText().toString();
            if (content.length() > 0) {
                if (EmojiUtil.isDeletePng(content)) {
                    ((Editable) etMain.getText())
                            .delete(content.length() - EmojiUtil.emojiFormat.length(),
                                    content.length());
                } else {
                    ((Editable) etMain.getText()).delete(content.length() - 1, content.length());
                }
            }
        }
    };

    public ChatKeyBoard(Context context, AttributeSet attrs) {
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
        funcLayout = getView(R.id.bjmgf_message_chat_fl);
        etMain = getView(R.id.bjmgf_message_chat_et);
        mBtnSend = (ImageView) findViewById(R.id.bjmgf_message_chat_sound_btn);

        ivFace.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        llKeyBoardDel.setOnClickListener(this);
    }

    protected void initFuncView() {
        View keyboardView = inflateFunc();
        funcLayout.addFuncView(FUNC_TYPE_EMOTION, keyboardView);
        emoticonsViewPager = getView(R.id.bjmgf_message_chat_view_epv);
        emoticonsIndicatorView = getView(R.id.bjmgf_message_view_eiv);
        emoticonsToolBarView = getView(R.id.bjmgf_message_chat_view_etv);
        emoticonsToolBarView.setOnToolBarItemClickListener(this);
        funcLayout.setOnFuncChangeListener(this);
        emoticonsViewPager.setOnIndicatorListener(this);

        moreView = inflateMore();
        funcLayout.addFuncView(FUNC_TYPE_MORE, moreView);
        vpFunction = getView(R.id.bjmgf_message_chat_function_vp);
        llDot = getView(R.id.bjmgf_message_chat_function_dots);
        initMoreView();
    }

    private void initMoreView() {
        List<Function> listFunction = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Function f = null;
            try {
                f = new Function(functionArray[i], BitmapFactory.decodeStream(
                        getContext().getAssets().open("function/function_" + i + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            listFunction.add(f);
        }
        for (int i = 0; i < 2; i++) {
            views.add(viewPagerItem(i, listFunction));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(16, 16);
            llDot.addView(dotsItem(i), params);
        }
        ChatFunctionAdapter adapter = new ChatFunctionAdapter(views);
        vpFunction.setAdapter(adapter);
        llDot.getChildAt(0).setSelected(true);
        vpFunction.addOnPageChangeListener(new PageChange());
    }

    /**KeyBoardListener
     * 获取viewpager item
     */
    private View viewPagerItem(int position, final List<Function> list) {
        int columns = 4;
        int rows = 2;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bjmgf_message_chat_function_gridview, null);
        GridView gv = (GridView) view.findViewById(R.id.bjmgf_message_chat_function_gv);
        List<Function> subList = new ArrayList<>();
        subList.addAll(list
                .subList(position * (columns * rows),
                        (columns * rows) * (position + 1) > list
                                .size() ? list.size() : (columns
                                * rows)
                                * (position + 1)));
        functionAdapter = new FunctionAdapter(getContext(), subList,
                new FunctionAdapter.Listener() {
                    @Override
                    public void functionSelected(int position) {
                        listener.functionSelected(position);
                    }
                });
        gv.setAdapter(functionAdapter);
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
                Global.MESSAGES.get(index).setDraft(s.toString());
                if (count > 0) {
                    isSend = true;
                    mBtnSend.setImageResource(R.mipmap.bjmgf_message_chat_send_btn);
                    int length = s.toString().length();
                    if (length > 0) {
                        String sign = s.toString().substring(start, length);
                        String emojiId = EmojiGlobal.getInstance().getEmojisCode().get(sign);
                        if (emojiId != null) {
                            ((Editable) etMain.getText()).delete(start, length);
                            EmojiUtil.insert(etMain, EmojiUtil
                                    .getFace(getContext(), "face/emojis/EmojiS_" + emojiId + ".png",
                                            etMain));
                        }
                    }
                } else if (s.length() == 0) {
                    isSend = false;
                    mBtnSend.setImageResource(R.mipmap.bjmgf_message_chat_sound_btn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    protected void setAdapter(PageSetAdapter pageSetAdapter) {
        if (null != pageSetAdapter) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            emoticonsToolBarView.reset();
            if (null != pageSetEntities) {
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    emoticonsToolBarView.addToolItemView(pageSetEntity);
                }
            }
        }
        emoticonsViewPager.setAdapter(pageSetAdapter);
    }

    public void addEmoticons(List<PageSetEntity> pageSetEntityList) {
        pageSetAdapter.getPageSetEntityList().clear();
        for (PageSetEntity p : pageSetEntityList) {
            pageSetAdapter.add(p);
        }
        setAdapter(pageSetAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bjmgf_message_chat_btn_face) {
            isClickFunction = true;
            funcLayout.updateHeight(keyBoardHeight);
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (id == R.id.bjmgf_message_chat_btn_more) {
            isClickFunction = true;
            funcLayout.updateHeight(keyBoardHeight);
            toggleFuncView(FUNC_TYPE_MORE);
        } else if (id == R.id.bjmgf_message_chat_sound_btn) {
            if (isSend) {
                if (null != listener) {
                    listener.sendMessage(etMain.getText().toString());
                    etMain.setText("");
                }
            } else {
                if (etMain.isEnabled()) {
                    etMain.setEnabled(false);
                } else {
                    etMain.setEnabled(true);
                }
                reset();
            }
        } else if (id == R.id.bjmgf_message_keyboard_del) {
            if (null != listener) {
                listener.delMessage();
            }
        }
    }

    protected void toggleFuncView(int key) {
        funcLayout.toggleFuncView(key, isSoftKeyboardPop(), etMain);
    }

    @Override
    public void onFuncChange(int key) {

    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        emoticonsViewPager.setCurrentPageSet(pageSetEntity);
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        emoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        emoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        emoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void OnSoftPop(int height) {
        if (mSoftKeyboardHeight != height) {
            mSoftKeyboardHeight = height;
            KeyBoardUtil.setDefKeyboardHeight(getContext(), mSoftKeyboardHeight);
        }
        funcLayout.updateHeight(height);
        funcLayout.setVisibility(true);
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
        KeyBoardUtil.closeSoftKeyboard(this);
        funcLayout.hideAllFuncView();
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

    public PageViewInstantiateListener<PageEntity> getEmoticonPageViewInstantiateItem() {
        return new PageViewInstantiateListener<PageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, PageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmoticonPageView pageView = new EmoticonPageView(container.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        EmoticonsAdapter adapter = new EmoticonsAdapter(container.getContext(),
                                pageEntity.getEmoticonList(), listener, emojiListener);
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
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

    public void setListener(KeyBoardListener listener) {
        this.listener = listener;
    }

    public void setMessageIndex(int index) {
        this.index = index;
    }

    public EditText getEditText() {
        return etMain;
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return emoticonsToolBarView;
    }
}
