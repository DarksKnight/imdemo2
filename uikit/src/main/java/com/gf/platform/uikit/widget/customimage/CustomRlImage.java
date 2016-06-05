package com.gf.platform.uikit.widget.customimage;

import com.gf.platform.uikit.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by apple on 16/6/5.
 */

public class CustomRlImage extends RelativeLayout {

    private Bitmap bitMap = null;

    public CustomRlImage(Context context) {
        super(context);
    }

    public CustomRlImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRlImage(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBitmap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null != bitMap) {
            canvas.drawBitmap(bitMap,
                    getMeasuredWidth() - getResources().getDimension(R.dimen.gf_35dp),
                    getResources().getDimension(R.dimen.gf_8dp), new Paint());
        }
    }
}
