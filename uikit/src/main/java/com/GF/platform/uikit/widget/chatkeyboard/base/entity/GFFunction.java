package com.GF.platform.uikit.widget.chatkeyboard.base.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 功能
 * Created by sunhaoyang on 2016/3/3.
 */
public class GFFunction implements Parcelable {

    private String name;
    private Bitmap pic;
    private int resId;

    public GFFunction() {}

    public GFFunction(String name, int resId) {
        this.resId = resId;
        this.name = name;
    }

    public GFFunction(String name, Bitmap pic) {
        this.pic = pic;
        this.name = name;
    }

    public GFFunction(String name, Bitmap pic, int resId) {
        this.name = name;
        this.pic = pic;
        this.resId = resId;
    }

    protected GFFunction(Parcel in) {
        name = in.readString();
        pic = in.readParcelable(Bitmap.class.getClassLoader());
        resId = in.readInt();
    }

    public static final Creator<GFFunction> CREATOR = new Creator<GFFunction>() {
        @Override
        public GFFunction createFromParcel(Parcel in) {
            return new GFFunction(in);
        }

        @Override
        public GFFunction[] newArray(int size) {
            return new GFFunction[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(pic, flags);
        dest.writeInt(resId);
    }
}
