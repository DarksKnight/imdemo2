package com.GF.platform.uikit.widget.chatkeyboard.util;

import android.os.Environment;

import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFEmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFEmoticonPageSetEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class GFParseDataUtil {

    /**
     * 将face json转换
     *
     * @param name
     * @return
     */
    public static List<GFEmoticonEntity> parseEmoticons(String name) {
        List<GFEmoticonEntity> list = new ArrayList<>();
        try {
            String json = GFKeyBoardUtil.getFileFromSD(Environment.getExternalStorageDirectory() + File.separator + GFKeyBoardUtil.FOLDERNAME + File.separator + name + File.separator + name + ".json");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                GFEmoticonEntity entity = new GFEmoticonEntity();
                entity.setId(item.getString("ID"));
                entity.setName(item.getString("Name"));
                entity.setShow(item.getString("Show"));
                entity.setIconUri(Environment.getExternalStorageDirectory() + File.separator + GFKeyBoardUtil.FOLDERNAME + File.separator + name + File.separator + name + "_" + item.getString("Name") + ".png");
                entity.setShow(true);
                entity.setEmoji(false);
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获取表情页面
     * @param name
     * @return
     */
    public static GFEmoticonPageSetEntity<GFEmoticonEntity> getPageSetEntity(String name) {
        GFEmoticonPageSetEntity.Builder<GFEmoticonEntity> entityBuilder = new GFEmoticonPageSetEntity.Builder<>();
        entityBuilder.setEmoticonList(GFParseDataUtil.parseEmoticons(name));
        return new GFEmoticonPageSetEntity(entityBuilder);
    }
}
