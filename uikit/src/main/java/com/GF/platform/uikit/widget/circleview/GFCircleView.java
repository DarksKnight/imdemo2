package com.GF.platform.uikit.widget.circleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.GF.platform.uikit.R;

/**
 * Created by sunhaoyang on 2016/6/14.
 */

public class GFCircleView extends View {

    private Paint paint = null;
    private int color = Color.WHITE;

    public GFCircleView(Context context) {
        super(context);
    }

    public GFCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GFCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(getResources().getDimension(R.dimen.gf_5dp), getResources().getDimension(R.dimen.gf_5dp), getResources().getDimension(R.dimen.gf_5dp), paint);
    }

    public void setViewColor(int color) {
        this.color = color;
        invalidate();
    }
}
