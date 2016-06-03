package com.gf.platform.uikit.widget.chatkeyboard.base.entity;

import android.graphics.Bitmap;

/**
 * 功能
 * Created by sunhaoyang on 2016/3/3.
 */
public class Function {

    private String name;
    private Bitmap pic;
    private int resId;

    public Function () {}

    public Function(String name, int resId) {
        this.resId = resId;
        this.name = name;
    }

    public Function(String name, Bitmap pic) {
        this.pic = pic;
        this.name = name;
    }

    public Function(String name, Bitmap pic, int resId) {
        this.name = name;
        this.pic = pic;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
