package com.GF.platform.uikit.widget.chatkeyboard.base.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

public class GFPageSetEntity<T extends GFPageEntity> implements Serializable {

    protected final String uuid = UUID.randomUUID().toString();
    protected final int mPageCount;
    protected final boolean mIsShowIndicator;
    protected final LinkedList<T> mPageEntityList;
    protected final String mIconUri;
    protected final int mIconId;
    protected final String mSetName;
    protected final boolean isEmoji;

    public GFPageSetEntity(final Builder builder) {
        this.mPageCount = builder.pageCount;
        this.mIsShowIndicator = builder.isShowIndicator;
        this.mPageEntityList = builder.pageEntityList;
        this.mIconUri = builder.iconUri;
        this.mSetName = builder.setName;
        this.mIconId = builder.iconId;
        this.isEmoji = builder.isEmoji;
    }

    public String getIconUri() {
        return mIconUri;
    }

    public int getIconId() {
        return mIconId;
    }

    public int getPageCount() {
        return mPageEntityList == null ? 0 : mPageEntityList.size();
    }

    public LinkedList<T> getPageEntityList() {
        return mPageEntityList;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isShowIndicator() {
        return mIsShowIndicator;
    }

    public static class Builder<T extends GFPageEntity> {

        protected int pageCount;
        protected boolean isShowIndicator = true;
        protected LinkedList<T> pageEntityList = new LinkedList<>();
        protected String iconUri;
        protected int iconId;
        protected String setName;
        protected boolean isEmoji;

        public Builder setShowIndicator(boolean showIndicator) {
            isShowIndicator = showIndicator;
            return this;
        }

        public Builder addPageEntity(T pageEntityt) {
            pageEntityList.add(pageEntityt);
            return this;
        }

        public Builder setIconUri(String iconUri) {
            this.iconUri = iconUri;
            return this;
        }

        public Builder setIconId(int id) {
            this.iconId = id;
            return this;
        }

        public Builder isEmoji(boolean isEmoji) {
            this.isEmoji = isEmoji;
            return this;
        }

        public Builder setSetName(String setName) {
            this.setName = setName;
            return this;
        }

        public Builder() {
        }

        public GFPageSetEntity<T> build() {
            return new GFPageSetEntity<>(this);
        }
    }
}
