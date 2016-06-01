package com.gf.platform.gfplatform.widget.chatkeyboard.util;

import android.os.Environment;

import com.gf.platform.gfplatform.util.Util;
import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.EmoticonEntity;
import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.EmoticonPageSetEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhaoyang on 2016/4/20.
 */
public class ParseDataUtil {

    /**
     * 将face json转换
     *
     * @param name
     * @return
     */
    public static List<EmoticonEntity> parseEmoticons(String name) {
        List<EmoticonEntity> list = new ArrayList<>();
        try {
            String json = Util.getFileFromSD(Environment.getExternalStorageDirectory() + File.separator + KeyBoardUtil.FOLDERNAME + File.separator + name + File.separator + name + ".json");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                EmoticonEntity entity = new EmoticonEntity();
                entity.setId(item.getString("ID"));
                entity.setName(item.getString("Name"));
                entity.setShow(item.getString("Show"));
                entity.setIconUri(Environment.getExternalStorageDirectory() + File.separator + KeyBoardUtil.FOLDERNAME + File.separator + name + File.separator + name + "_" + item.getString("Name") + ".png");
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
    public static EmoticonPageSetEntity<EmoticonEntity> getPageSetEntity(String name) {
        EmoticonPageSetEntity.Builder<EmoticonEntity> entityBuilder = new EmoticonPageSetEntity.Builder<>();
        entityBuilder.setEmoticonList(ParseDataUtil.parseEmoticons(name));
        return new EmoticonPageSetEntity(entityBuilder);
    }
}
