package com.gf.platform.gfplatform.widget.chatkeyboard.base.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class EmoticonEntity implements Parcelable {

    private String iconUri;
    private String id;
    private String name;
    private String show;
    private String code;
    private String value;
    private String path;
    private boolean isShow;
    private boolean isEmoji;

    protected EmoticonEntity(Parcel in) {
        iconUri = in.readString();
        id = in.readString();
        name = in.readString();
        show = in.readString();
        code = in.readString();
        value = in.readString();
        path = in.readString();
        isShow = in.readByte() != 0;
        isEmoji = in.readByte() != 0;
    }

    public static final Creator<EmoticonEntity> CREATOR = new Creator<EmoticonEntity>() {
        @Override
        public EmoticonEntity createFromParcel(Parcel in) {
            return new EmoticonEntity(in);
        }

        @Override
        public EmoticonEntity[] newArray(int size) {
            return new EmoticonEntity[size];
        }
    };

    public boolean isEmoji() {
        return isEmoji;
    }

    public void setEmoji(boolean emoji) {
        isEmoji = emoji;
    }

    public EmoticonEntity() { }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iconUri);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(show);
        dest.writeString(code);
        dest.writeString(value);
        dest.writeString(path);
        dest.writeByte((byte) (isShow ? 1 : 0));
        dest.writeByte((byte) (isEmoji ? 1 : 0));
    }
}
