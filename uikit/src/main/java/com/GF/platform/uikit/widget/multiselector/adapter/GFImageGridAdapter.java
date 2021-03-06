package com.GF.platform.uikit.widget.multiselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.base.GFCommonAdapter;
import com.GF.platform.uikit.base.GFViewHolder;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.multiselector.entity.GFImage;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 */
public class GFImageGridAdapter extends GFCommonAdapter<GFImage> {

    private List<GFImage> mSelectedGFImages = new ArrayList<>();
    private int mGridWidth = 0;

    public GFImageGridAdapter(Context context, int column) {
        super(context, new ArrayList<GFImage>(), R.layout.bjmgf_pic_list_item_image);
        mGridWidth = GFUtil.getScreenWidth(context);
        mGridWidth = mGridWidth / column;
    }

    public void setData(List<GFImage> images) {
        mSelectedGFImages.clear();

        if(images != null && images.size()>0){
            data = images;
        }else{
            data.clear();
        }
        notifyDataSetChanged();
    }

    public void select(GFImage image) {
        if(mSelectedGFImages.contains(image)){
            mSelectedGFImages.remove(image);
        }else{
            mSelectedGFImages.add(image);
        }
        notifyDataSetChanged();
    }

    public void setDefaultSelected(ArrayList<String> resultList) {
        for(String path : resultList){
            GFImage GFImage = getImageByPath(path);
            if(GFImage != null){
                mSelectedGFImages.add(GFImage);
            }
        }
        if(mSelectedGFImages.size() > 0){
            notifyDataSetChanged();
        }
    }

    private GFImage getImageByPath(String path){
        if(data != null && data.size()>0){
            for(GFImage GFImage : data){
                if(GFImage.path.equalsIgnoreCase(path)){
                    return GFImage;
                }
            }
        }
        return null;
    }


    @Override
    public void convert(GFViewHolder GFViewHolder, GFImage gfImage, int position) {
        SimpleDraweeView image = GFViewHolder.getView(R.id.image);
        ImageView indicator = GFViewHolder.getView(R.id.checkmark);
        View mask = GFViewHolder.getView(R.id.mask);

        indicator.setVisibility(View.VISIBLE);
        if(mSelectedGFImages.contains(gfImage)){
            // 设置选中状态
            indicator.setImageResource(R.mipmap.btn_selected);
            mask.setVisibility(View.VISIBLE);
        }else{
            // 未选择
            indicator.setImageResource(R.mipmap.btn_unselected);
            mask.setVisibility(View.GONE);
        }
        File imageFile = new File(gfImage.path);
        ViewGroup.LayoutParams lp = image.getLayoutParams();
        lp.height = mGridWidth;
        lp.width = mGridWidth;
        image.setLayoutParams(lp);
        mask.setLayoutParams(lp);
        if (imageFile.exists()) {
            // 显示图片
            image.setController(GFUtil.getCommonController(image, "file://" + imageFile.getPath(), mGridWidth, mGridWidth));
        }else{
            image.setImageResource(R.mipmap.default_error);
        }
    }
}
