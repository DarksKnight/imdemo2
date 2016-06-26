package com.GF.platform.uikit.widget.chatkeyboard.base.command;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFKeyBoardCommand;
import com.GF.platform.uikit.widget.multiselector.GFMultiImageSelectorActivity;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFSelectPicCommand implements GFKeyBoardCommand {

    private Context context = null;

    public GFSelectPicCommand(Context context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Intent intent = new Intent(context, GFMultiImageSelectorActivity.class);
        // 最大可选择图片数量
        intent.putExtra(GFMultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(GFMultiImageSelectorActivity.EXTRA_SELECT_MODE, GFMultiImageSelectorActivity.MODE_MULTI);
        ((Activity) context).startActivityForResult(intent, 0);
    }
}
