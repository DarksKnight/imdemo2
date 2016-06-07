package com.GF.platform.gfplatform.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.GF.platform.gfplatform.R;
import com.GF.platform.uikit.widget.swipeback.SwipeBackActivityHelper;

/**
 * 基类fragmentactivity
 * Created by sunhaoyang on 2016/2/23.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    private RelativeLayout rlContent = null;
    private RelativeLayout rlTitle = null;
    private RelativeLayout rlBack = null;
    private TextView tvBack = null;
    private TextView tvTitle = null;
    //滑动返回并且关闭当前页面
    private SwipeBackActivityHelper helper = SwipeBackActivityHelper.getInstance();
    private boolean closeSwipeBack = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeContentView();
        initTemp();
        setContentLayout(getContentView());
        obtainParam(getIntent());
        //初始化滑动关闭页面
        helper.setDebuggable(true)
                .setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(3)
                .setNeedBackgroundShadow(false)
                .init(this);
        initView();
        initData();
    }

    protected void disableSwipeBack() {
        closeSwipeBack = true;
        helper.disableSwipeBack();
    }

    protected void obtainParam(Intent intent){}

    protected void beforeContentView() {}

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    public boolean isHideTitle() {
        return false;
    }

    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    protected void showToast (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initTemp() {
        setContentView(R.layout.bjmgf_common_temp);
        rlContent = getView(R.id.bjmgf_common_content_rl);
        rlTitle = getView(R.id.bjmgf_common_title_rl);
        rlBack = getView(R.id.bjmgf_common_back);
        tvBack = getView(R.id.bjmgf_common_title_tv);
        tvTitle = getView(R.id.bjmgf_common_title_tv);
        if (isHideTitle()) {
            rlTitle.setVisibility(View.GONE);
        } else {
            rlTitle.setVisibility(View.VISIBLE);
        }

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.finish();
            }
        });
    }

    public void setContentLayout(int resId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(resId, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        if (null != rlContent) {
            rlContent.addView(contentView);
        }
    }

    protected void setBackInfo(String text) {
        if (null != tvBack) {
            tvBack.setVisibility(View.VISIBLE);
            tvBack.setText(text);
        }
    }

    protected void setTitleText(String text) {
        if (null != tvTitle) {
            tvTitle.setText(text);
        }
    }

    @Override
    public void onBackPressed() {
        if (closeSwipeBack) {
            super.onBackPressed();
        } else {
            helper.finish();
        }
    }
}
