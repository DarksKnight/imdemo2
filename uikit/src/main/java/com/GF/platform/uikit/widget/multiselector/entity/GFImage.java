package com.GF.platform.uikit.widget.multiselector.entity;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class GFImage {
    public String path;
    public String name;
    public long time;

    public GFImage(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            GFImage other = (GFImage) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
