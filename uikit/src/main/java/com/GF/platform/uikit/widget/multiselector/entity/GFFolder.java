package com.GF.platform.uikit.widget.multiselector.entity;

import android.text.TextUtils;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class GFFolder {
    public String name;
    public String path;
    public GFImage cover;
    public List<GFImage> GFImages;

    @Override
    public boolean equals(Object o) {
        try {
            GFFolder other = (GFFolder) o;
            return TextUtils.equals(other.path, path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
