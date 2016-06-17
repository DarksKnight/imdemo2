package com.GF.platform.uikit.widget.multiselector.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.base.GFCommonAdapter;
import com.GF.platform.uikit.base.GFViewHolder;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.multiselector.entity.GFFolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 文件夹Adapter
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 */
public class GFFolderAdapter extends GFCommonAdapter<GFFolder> {
    int lastSelected = 0;

    public GFFolderAdapter(Context context) {
        super(context, null, R.layout.list_item_folder);
    }

    public void setData(List<GFFolder> GFFolders) {
        if (GFFolders != null && GFFolders.size() > 0) {
            data = GFFolders;
        } else {
            data.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public void convert(GFViewHolder GFViewHolder, GFFolder gfFolder, int position) {
        SimpleDraweeView cover = GFViewHolder.getView(R.id.cover);
        TextView name = GFViewHolder.getView(R.id.name);
        TextView path = GFViewHolder.getView(R.id.path);
        TextView size = GFViewHolder.getView(R.id.size);
        ImageView indicator = GFViewHolder.getView(R.id.indicator);

        if (position == 0) {
            name.setText("All Images");
            path.setText("/sdcard");
            size.setText(String.format("%d%s",
                    getTotalImageSize(), "Shot"));
            if (data.size() > 0) {
                GFFolder f = data.get(0);
                cover.setController(GFUtil.getCommonController(cover, "file://" + f.cover.path, (int)context.getResources().getDimension(R.dimen.gf_72dp), (int)context.getResources().getDimension(R.dimen.gf_72dp)));
            }
        } else {
            name.setText(gfFolder.name);
            path.setText(gfFolder.path);
            if (gfFolder.GFImages != null) {
                size.setText(String.format("%d%s", gfFolder.GFImages.size(), "Shot"));
            } else {
                size.setText("*" + "Shot");
            }
            // 显示图片
            if (gfFolder.cover != null) {
                cover.setController(GFUtil.getCommonController(cover, "file://" + gfFolder.cover.path, (int) context.getResources().getDimension(R.dimen.gf_72dp), (int) context.getResources().getDimension(R.dimen.gf_72dp)));
            } else {
                cover.setImageResource(R.mipmap.default_error);
            }
        }
        if (lastSelected == position) {
            indicator.setVisibility(View.VISIBLE);
        } else {
            indicator.setVisibility(View.INVISIBLE);
        }
    }

    private int getTotalImageSize() {
        int result = 0;
        if (data != null && data.size() > 0) {
            for (GFFolder f : data) {
                result += f.GFImages.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }
}
