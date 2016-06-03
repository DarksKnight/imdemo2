package com.gf.platform.gfplatform.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.gf.platform.uikit.Constant;

/**
 * 消息
 * Created by sunhaoyang on 2016/2/22.
 */
public class Message implements Parcelable {

    private String nickName = "";
    private String info = "";
    private String date = "";
    private String head = "";
    private Bitmap face = null;
    private Category category = null;
    private Bitmap expression = null;
    private boolean isTop = false;
    private int oldPosition = 0;
    private String draft = "";
    private boolean checked = false;
    private boolean showSelected = false;
    private int type = Constant.MSG_TYPE_TEXT;

    protected Message(Parcel in) {
        nickName = in.readString();
        info = in.readString();
        date = in.readString();
        head = in.readString();
        face = in.readParcelable(Bitmap.class.getClassLoader());
        expression = in.readParcelable(Bitmap.class.getClassLoader());
        isTop = in.readByte() != 0;
        oldPosition = in.readInt();
        draft = in.readString();
        checked = in.readByte() != 0;
        showSelected = in.readByte() != 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickName);
        dest.writeString(info);
        dest.writeString(date);
        dest.writeString(head);
        dest.writeParcelable(face, flags);
        dest.writeParcelable(expression, flags);
        dest.writeByte((byte) (isTop ? 1 : 0));
        dest.writeInt(oldPosition);
        dest.writeString(draft);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (showSelected ? 1 : 0));
    }

    public enum Category {
        NORMAL_ME, NORMAL_YOU, NURTURE
    }

    public boolean isShowSelected() {
        return showSelected;
    }

    public void setShowSelected(boolean showSelected) {
        this.showSelected = showSelected;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public int getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Bitmap getFace() {
        return face;
    }

    public void setFace(Bitmap face) {
        this.face = face;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Bitmap getExpression() {
        return expression;
    }

    public void setExpression(Bitmap expression) {
        this.expression = expression;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getType() {
        if (null == expression) {
            type = Constant.MSG_TYPE_TEXT;
        } else if (null != expression) {
            type = Constant.MSG_TYPE_EXPRESSION;
        }
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Message() {
    }

    public Message(String nickName, String info, String date, String head, Category category, boolean isTop) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.category = category;
        this.isTop = isTop;
    }

    public Message(String nickName, String info, String date, String head, Category category, Bitmap expression, boolean isTop) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.category = category;
        this.expression = expression;
        this.isTop = isTop;
    }

    public Message(String nickName, String info, String date, String head, Bitmap face) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.face = face;
    }
}
