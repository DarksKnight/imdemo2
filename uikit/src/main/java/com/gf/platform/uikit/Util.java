package com.gf.platform.uikit;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by sunhaoyang on 2016/6/2.
 */

public class Util {

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        return outSize.x;
    }

    /**
     * 显示toast
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
