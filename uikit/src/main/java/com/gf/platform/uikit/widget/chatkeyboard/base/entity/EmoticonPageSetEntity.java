package com.gf.platform.uikit.widget.chatkeyboard.base.entity;


import com.gf.platform.uikit.widget.chatkeyboard.base.ports.PageViewInstantiateListener;

import java.util.List;

/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class EmoticonPageSetEntity<T> extends PageSetEntity<PageEntity> {

    final List<T> mEmoticonList;
    final static int defaultLine = 2;
    final static int defaultRow = 4;

    public EmoticonPageSetEntity(final Builder builder) {
        super(builder);
        this.mEmoticonList = builder.emoticonList;
    }

    public List<T> getEmoticonList() {
        return mEmoticonList;
    }

    public static class Builder<T> extends PageSetEntity.Builder {

        /**
         * 每页行数
         */
        protected int line = defaultLine;
        /**
         * 每页列数
         */
        protected int row = defaultRow;
        /**
         * 删除按钮
         */
        protected PageEntity.DelBtnStatus delBtnStatus = PageEntity.DelBtnStatus.GONE;
        /**
         * 表情集数据源
         */
        protected List<T> emoticonList;

        protected boolean isEmoji = false;

        protected PageViewInstantiateListener pageViewInstantiateListener;

        public Builder() {
        }

        public Builder setLine(int line) {
            this.line = line;
            return this;
        }

        public Builder setRow(int row) {
            this.row = row;
            return this;
        }

        public Builder setShowDelBtn(PageEntity.DelBtnStatus showDelBtn) {
            delBtnStatus = showDelBtn;
            return this;
        }

        public Builder setEmoticonList(List<T> emoticonList) {
            this.emoticonList = emoticonList;
            return this;
        }

        public Builder setIPageViewInstantiateItem(PageViewInstantiateListener pageViewInstantiateListener) {
            this.pageViewInstantiateListener = pageViewInstantiateListener;
            return this;
        }

        public Builder setShowIndicator(boolean showIndicator) {
            isShowIndicator = showIndicator;
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

        public Builder setSetName(String setName) {
            this.setName = setName;
            return this;
        }

        public Builder isEmoji(boolean isEmoji) {
            this.isEmoji = isEmoji;
            return this;
        }

        public EmoticonPageSetEntity<T> build() {
            if (null != emoticonList) {
                int emoticonSetSum = emoticonList.size();
                int everyPageMaxSum = row * line;
                pageCount = (int) Math.ceil((double) emoticonList.size() / everyPageMaxSum);

                if (!pageEntityList.isEmpty()) {
                    pageEntityList.clear();
                }

                for (int i = 0; i < pageCount; i++) {
                    int start = i * everyPageMaxSum - 1 ;
                    if (start < 0) {
                        start = 0;
                    }
                    int end = (start + everyPageMaxSum) > emoticonSetSum ? emoticonSetSum
                            : (start + everyPageMaxSum);

                    PageEntity emoticonPageEntity = new PageEntity();
                    emoticonPageEntity.setLine(line);
                    emoticonPageEntity.setRow(row);
                    emoticonPageEntity.setDelBtnStatus(delBtnStatus);
                    if (isEmoji) {
                        emoticonPageEntity.setEmoticonList(emoticonList.subList(start, end - 1));
                        EmoticonEntity entity = new EmoticonEntity();
                        entity.setId("-1");
                        entity.setName("del_normal");
                        entity.setShow("删除");
                        entity.setEmoji(true);
                        entity.setIconUri("face/emoji/Emoji_del_normal.png");
                        emoticonPageEntity.getEmoticonList().add(entity);
                    } else {
                        emoticonPageEntity.setEmoticonList(emoticonList.subList(start, end));
                    }
                    emoticonPageEntity.setIPageViewInstantiateItem(pageViewInstantiateListener);
                    pageEntityList.add(emoticonPageEntity);
                }
            }
            return new EmoticonPageSetEntity<>(this);
        }
    }
}
