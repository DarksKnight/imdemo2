package com.GF.platform.uiview.base;

import android.content.Intent;

/**
 * 生命周期接口
 * Created by sunhaoyang on 2016/6/9.
 */

public interface GFViewPorts {

    void onResume();

    void onPause();

    void finish();

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
