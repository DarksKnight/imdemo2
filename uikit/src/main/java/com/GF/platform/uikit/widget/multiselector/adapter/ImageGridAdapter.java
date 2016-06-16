package com.GF.platform.uikit.widget.multiselector.adapter;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.widget.multiselector.bean.Image;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 */
public class ImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();

    final int mGridWidth;

    public ImageGridAdapter(Context context, boolean showCamera, int column){
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        }else{
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / column;
    }
    /**
     * 显示选择指示器
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b){
        if(showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera(){
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     * @param image
     */
    public void select(Image image) {
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
        }else{
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }

    /**
     * 通过图片路径设置默认选择
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for(String path : resultList){
            Image image = getImageByPath(path);
            if(image != null){
                mSelectedImages.add(image);
            }
        }
        if(mSelectedImages.size() > 0){
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path){
        if(mImages != null && mImages.size()>0){
            for(Image image : mImages){
                if(image.path.equalsIgnoreCase(path)){
                    return image;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if(images != null && images.size()>0){
            mImages = images;
        }else{
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(showCamera){
            return position==0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImages.size()+1 : mImages.size();
    }

    @Override
    public Image getItem(int i) {
        if(showCamera){
            if(i == 0){
                return null;
            }
            return mImages.get(i-1);
        }else{
            return mImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(isShowCamera()){
            if(i == 0){
                view = mInflater.inflate(R.layout.list_item_camera, viewGroup, false);
                return view;
            }
        }

        ViewHolder holder;
        if(view == null){
            view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Image data = getItem(i);
        // 处理单选和多选状态
        if(showSelectIndicator){
            holder.indicator.setVisibility(View.VISIBLE);
            if(mSelectedImages.contains(data)){
                // 设置选中状态
                holder.indicator.setImageResource(R.mipmap.btn_selected);
                holder.mask.setVisibility(View.VISIBLE);
            }else{
                // 未选择
                holder.indicator.setImageResource(R.mipmap.btn_unselected);
                holder.mask.setVisibility(View.GONE);
            }
        }else{
            holder.indicator.setVisibility(View.GONE);
        }
        File imageFile = new File(data.path);
        ViewGroup.LayoutParams lp = holder.image.getLayoutParams();
        lp.height = mGridWidth;
        lp.width = mGridWidth;
        holder.image.setLayoutParams(lp);
        holder.mask.setLayoutParams(lp);
        if (imageFile.exists()) {
            // 显示图片
            ImageDecodeOptions options = ImageDecodeOptions.newBuilder()
                    .setUseLastFrameForPreview(true)
                    .setDecodeAllFrames(true)
                    .setDecodePreviewFrame(true)
                    .build();
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + imageFile.getPath()))
                    .setImageDecodeOptions(options)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(mGridWidth, mGridWidth))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(holder.image.getController())
                    .build();
            holder.image.setController(controller);
        }else{
            holder.image.setImageResource(R.mipmap.default_error);
        }

        return view;
    }

    class ViewHolder {
        SimpleDraweeView image;
        ImageView indicator;
        View mask;

        ViewHolder(View view){
            image = (SimpleDraweeView) view.findViewById(R.id.image);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);
        }
    }

}
