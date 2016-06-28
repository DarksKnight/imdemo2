package com.GF.platform.uikit.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.GF.platform.uikit.GFConstant;

/**
 * 消息
 * Created by sunhaoyang on 2016/2/22.
 */
public class GFMessage implements Parcelable {
    private String msgId = "";
    private String nickName = "";
    private String info = "";
    private String date = "";
    private String head = "";
    private Bitmap face = null;
    private Category category = null;
    private Bitmap expression = null;
    private String picture = "";
    private boolean isTop = false;
    private int oldPosition = 0;
    private String draft = "";
    private boolean checked = false;
    private boolean showSelected = false;
    private long audioTime = 0;
    private String audioPath = "";
    private boolean isSending = false;

    public String getPicture() {
        if (picture.trim().contains("http")) {
            return picture;
        } else {
            return "file://" +picture;
        }
    }

    public String getPath() {
        return picture;
    }

    public boolean isPic() {
        if (picture.trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isSending() {
        return isSending;
    }

    public void setSending(boolean sending) {
        isSending = sending;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    private int type = GFConstant.MSG_TYPE_TEXT;

    protected GFMessage(Parcel in) {
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

    public static final Creator<GFMessage> CREATOR = new Creator<GFMessage>() {
        @Override
        public GFMessage createFromParcel(Parcel in) {
            return new GFMessage(in);
        }

        @Override
        public GFMessage[] newArray(int size) {
            return new GFMessage[size];
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

    public long getAudioTime() {
        return audioTime;
    }

    public void setAudioTime(long audioTime) {
        this.audioTime = audioTime;
    }

    public int getType() {
        if (null == expression) {
            if (picture.trim().length() > 0) {
                type = GFConstant.MSG_TYPE_PIC;
            } else if (audioTime > 0) {
                type = GFConstant.MSG_TYPE_AUDIO;
            } else {
                type = GFConstant.MSG_TYPE_TEXT;
            }
        } else if (null != expression) {
            type = GFConstant.MSG_TYPE_EXPRESSION;
        }
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GFMessage() {
    }

    public GFMessage(String nickName, String info, String date, String head, Category category, boolean isTop) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.category = category;
        this.isTop = isTop;
    }

    public GFMessage(String nickName, String info, String date, String head, Category category, Bitmap expression, boolean isTop) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.category = category;
        this.expression = expression;
        this.isTop = isTop;
    }

    public GFMessage(String nickName, String info, String date, String head, Bitmap face) {
        this.nickName = nickName;
        this.info = info;
        this.date = date;
        this.head = head;
        this.face = face;
    }
}
