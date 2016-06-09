package com.GF.platform.gfplatform;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.GF.platform.gfplatform.base.BaseFragmentActivity;
import com.GF.platform.gfplatform.ui.fragment.ContactFragment;
import com.GF.platform.gfplatform.ui.fragment.ExploreFragment;
import com.GF.platform.gfplatform.ui.fragment.GameFragment;
import com.GF.platform.gfplatform.ui.fragment.message.MessageFragment;
import com.GF.platform.uikit.EmojiGlobal;
import com.GF.platform.uikit.util.Util;
import com.GF.platform.uikit.widget.badgerview.BadgeView;
import com.GF.platform.uikit.widget.circleimageview.CircleImageView;
import com.GF.platform.uikit.widget.customviewpager.CustomViewPager;
import com.GF.platform.uikit.widget.slidecontent.SlideContent;
import com.GF.platform.uikit.widget.slidemenu.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MainTabActivity extends BaseFragmentActivity {

    private CircleImageView faceIv = null;
    private TabLayout tbMain = null;
    private CustomViewPager vpMain = null;
    private TabFragmentAdapter adapter = null;
    private SlidingMenu sm = null;
    private float oldLocationX = 0;
    private float oldLocationY = 0;
    private BadgeView bv = null;
    private SlideContent slideContent = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        faceIv = getView(R.id.bjmgf_main_face_iv);
        tbMain = getView(R.id.bjmgf_main_tab);
        vpMain = getView(R.id.bjmgf_main_vp);
        sm = getView(R.id.bjmgf_main_menu);
        slideContent = getView(R.id.bjmgf_main_content);
        sm.setSlideContent(slideContent);
        disableSwipeBack();
    }

    @Override
    protected void initData() {
        sm.setBackgroundDrawable(new BitmapDrawable(
                Util.doBlur(BitmapFactory.decodeResource(getResources(), R.mipmap.demo_face), 15, false)));
        tbMain.setTabMode(TabLayout.MODE_FIXED);
        Fragment fMessage = new MessageFragment();
        Fragment fGame = new GameFragment();
        Fragment fContact = new ContactFragment();
        Fragment fExplore = new ExploreFragment();
        List<Fragment> list = new ArrayList<>();
        list.add(fGame);
        list.add(fMessage);
        list.add(fContact);
        list.add(fExplore);
        adapter = new TabFragmentAdapter(getSupportFragmentManager(), list);
        vpMain.setAdapter(adapter);
        tbMain.setupWithViewPager(vpMain);
        resetTab();
        EmojiGlobal.getInstance().init(this);

        faceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.toggle();
            }
        });

        tbMain.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resetTabImg();
                setTabImg(tab.getPosition(), tab);
                vpMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                resetTabImg();
                setTabImg(tab.getPosition(), tab);
                vpMain.setCurrentItem(tab.getPosition());
            }
        });
        tbMain.getTabAt(1).select();
    }

    private void setTabImg(int index, TabLayout.Tab t) {
        switch (index) {
            case 0:
                ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_game_selected);
                break;
            case 1:
                ImageView iv = (ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv);
                iv.setImageResource(R.mipmap.bjmgf_main_tab_message_selected);
                bv = new BadgeView(this, iv);
                bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                bv.setTextSize(10);
                bv.setBadgeMargin((int)getResources().getDimension(R.dimen.gf_12dp) ,(int)getResources().getDimension(R.dimen.gf_3dp));
                bv.setText("1");
                bv.show();
                break;
            case 2:
                ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_contact_selected);
                break;
            case 3:
                ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_explore_selected);
                break;
            default:
                break;
        }
    }

    private void resetTabImg() {
        int tabSize = tbMain.getTabCount();
        for (int i = 0; i < tabSize; i++) {
            TabLayout.Tab t = tbMain.getTabAt(i);
            switch (i) {
                case 0:
                    ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_game);
                    break;
                case 1:
                    ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_message);
                    break;
                case 2:
                    ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_contact);
                    break;
                case 3:
                    ((ImageView) t.getCustomView().findViewById(R.id.bjmgf_main_tab_iv)).setImageResource(R.mipmap.bjmgf_main_tab_explore);
                    break;
                default:
                    break;
            }
        }
    }

    private void resetTab() {
        int tabSize = tbMain.getTabCount();
        for (int i = 0; i < tabSize; i++) {
            TabLayout.Tab t = tbMain.getTabAt(i);
            switch (i) {
                case 0:
                    t.setCustomView(getTabView(this, R.mipmap.bjmgf_main_tab_game));
                    break;
                case 1:
                    t.setCustomView(getTabView(this, R.mipmap.bjmgf_main_tab_message));
                    break;
                case 2:
                    t.setCustomView(getTabView(this, R.mipmap.bjmgf_main_tab_contact));
                    break;
                case 3:
                    t.setCustomView(getTabView(this, R.mipmap.bjmgf_main_tab_explore));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            oldLocationX = ev.getX();
            oldLocationY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (oldLocationX == ev.getX() && oldLocationY == ev.getY()) {
                if (sm.isOpen()) {
                    sm.toggle();
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean isHideTitle() {
        return true;
    }

    /**
     * 设置tab中item的内容
     *
     * @param context
     * @param id
     * @return
     */
    private View getTabView(Context context, int id) {
        View v = View.inflate(context, R.layout.bjmgf_main_tab, null);
        ImageView iv = (ImageView) v.findViewById(R.id.bjmgf_main_tab_iv);
        iv.setImageResource(id);
        return v;
    }
}