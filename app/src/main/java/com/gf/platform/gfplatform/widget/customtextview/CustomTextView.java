package com.gf.platform.gfplatform.widget.customtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gf.platform.gfplatform.R;

public class CustomTextView extends TextView {

    // 需要从xml中读取的各个方向图片的宽和高
    private int leftHeight = -1;
    private int leftWidth = -1;
    private int rightHeight = -1;
    private int rightWidth = -1;
    private int topHeight = -1;
    private int topWidth = -1;
    private int bottomHeight = -1;
    private int bottomWidth = -1;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // super一定要在我们的代码之前配置文件
        init(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // super一定要在我们的代码之前配置文件
        init(context, attrs, defStyle);
    }

    /**
     * 初始化读取参数
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        // TypeArray中含有我们需要使用的参数
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView, defStyle, 0);
        if (a != null) {
            // 获得参数个数
            int count = a.getIndexCount();
            int index = 0;
            // 遍历参数。先将index从TypedArray中读出来，
            // 得到的这个index对应于attrs.xml中设置的参数名称在R中编译得到的数
            // 这里会得到各个方向的宽和高
            for (int i = 0; i < count; i++) {
                index = a.getIndex(i);
                switch (index) {
                    case R.styleable.CustomTextView_bottom_height:
                        bottomHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_bottom_width:
                        bottomWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_left_height:
                        leftHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_left_width:
                        leftWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_right_height:
                        rightHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_right_width:
                        rightWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_top_height:
                        topHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.CustomTextView_top_width:
                        topWidth = a.getDimensionPixelSize(index, -1);
                        break;
                }
            }

            // 获取各个方向的图片，按照：左-上-右-下 的顺序存于数组中
            Drawable[] drawables = getCompoundDrawables();
            int dir = 0;
            // 0-left; 1-top; 2-right; 3-bottom;
            for (Drawable drawable : drawables) {
                // 设定图片大小
                setImageSize(drawable, dir++);
            }
            // 将图片放回到TextView中
            setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                    drawables[3]);

        }

    }

    /**
     * 设定图片的大小
     */
    private void setImageSize(Drawable d, int dir) {
        if (d == null) {
            return;
        }

        int height = -1;
        int width = -1;
        // 根据方向给宽和高赋值
        switch (dir) {
            case 0:
                // left
                height = leftHeight;
                width = leftWidth;
                break;
            case 1:
                // top
                height = topHeight;
                width = topWidth;
                break;
            case 2:
                // right
                height = rightHeight;
                width = rightWidth;
                break;
            case 3:
                // bottom
                height = bottomHeight;
                width = bottomWidth;
                break;
        }
        // 如果有某个方向的宽或者高没有设定值，则不去设定图片大小
        if (width != -1 && height != -1) {
            d.setBounds(0, 0, width, height);
        }
    }
}
