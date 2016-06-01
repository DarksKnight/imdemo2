package com.gf.platform.gfplatform.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.gf.platform.gfplatform.R;
import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.EmojiSpan;
import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.EmoticonEntity;
import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.EmoticonPageSetEntity;
import com.gf.platform.gfplatform.widget.chatkeyboard.util.KeyBoardUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * emoji工具类
 * Created by sunhaoyang on 2016/2/24.
 */
public class EmojiUtil {
    //emoji表情格式
    public static String emojiFormat = "#[face/emojis/EmojiS_000.png]#";
    //emoji表情正则表达式
    public static String emojiRegex = "(\\#\\[face/emojis/EmojiS_)\\d{3}(.png\\]\\#)";

    /**
     * 把emoji_code json转换
     *
     * @param context
     * @return
     */
    public static Map<String, String> parseEmojiCode(Context context) {
        InputStream isEmojiCode = context.getResources().openRawResource(R.raw.emojicode);
        Map<String, String> maps = new HashMap<>();
        try {
            byte[] bEmojiCode = new byte[isEmojiCode.available()];
            isEmojiCode.read(bEmojiCode);
            String jEmojiCode = new String(bEmojiCode, "UTF-8");
            JSONArray jEmojiCodeArray = new JSONArray(jEmojiCode);
            for (int i = 0; i < jEmojiCodeArray.length(); i++) {
                JSONObject item = jEmojiCodeArray.getJSONObject(i);
                String id = "";
                int val = Integer.parseInt(item.getString("id"));
                if (val >= 100) {
                    id = item.getString("id");
                } else if (val < 100 && val >= 10) {
                    id = "0" + item.getString("id");
                } else {
                    id = "00" + item.getString("id");
                }
                maps.put(item.getString("sign"), id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    /**
     * 把emoji json转换
     *
     * @param context
     * @return
     */
    public static List<EmoticonEntity> parseEmoji(Context context) {
        List<EmoticonEntity> list = new ArrayList<>();
        InputStream isEmoji = context.getResources().openRawResource(R.raw.emoji);
        try {
            byte[] bEmoji = new byte[isEmoji.available()];
            isEmoji.read(bEmoji);
            String jEmoji = new String(bEmoji, "UTF-8");
            JSONArray jEmojiArray = new JSONArray(jEmoji);
            for (int i = 0; i < jEmojiArray.length(); i++) {
                JSONObject item = jEmojiArray.getJSONObject(i);
                String name = "";
                int val = Integer.parseInt(item.getString("Name"));
                if (val >= 100) {
                    name = item.getString("Name");
                } else if (val < 100 && val >= 10) {
                    name = "0" + item.getString("Name");
                } else {
                    name = "00" + item.getString("Name");
                }
                EmoticonEntity entity = new EmoticonEntity();
                entity.setId(item.getString("ID"));
                entity.setName(name);
                entity.setShow(item.getString("Show"));
                entity.setEmoji(true);
                entity.setIconUri("face/emoji/Emoji_" + name + ".png");
                list.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static EmoticonPageSetEntity<EmoticonEntity> getPageSetEntity(Context context) {
        EmoticonPageSetEntity.Builder<EmoticonEntity> entityBuilder = new EmoticonPageSetEntity.Builder<>();
        entityBuilder.setEmoticonList(parseEmoji(context));
        return new EmoticonPageSetEntity(entityBuilder);
    }

    /**
     * 获取表情，显示在textview和edittext中
     *
     * @param context
     * @param png
     * @return
     */
    public static SpannableStringBuilder getFace(Context context, String png, TextView tv) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/emoji/Emoji_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open(png));
            Drawable drawable =new BitmapDrawable(bmp);
            drawable.setBounds(0, 0, KeyBoardUtil.getFontHeight(tv), KeyBoardUtil.getFontHeight(tv));
            EmojiSpan span = new EmojiSpan(drawable);
            sb.setSpan(span, sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * 判断当前内容是否表情
     *
     * @param content
     * @return
     */
    public static boolean isDeletePng(String content) {
        if (content.length() >= EmojiUtil.emojiFormat.length()) {
            String checkStr = content.substring(content.length() - EmojiUtil.emojiFormat.length(),
                    content.length());
            Pattern p = Pattern.compile(EmojiUtil.emojiRegex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    /**
     * 转换emoji表情
     *
     * @param context
     * @param content
     * @return
     */
    public static SpannableStringBuilder convert(Context context, String content, TextView tv) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        Pattern p = Pattern.compile(EmojiUtil.emojiRegex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            String png = tempText.substring("#[".length(), tempText.length() - "]#".length());
            try {
                Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open(png));
                Drawable drawable =new BitmapDrawable(bmp);
                drawable.setBounds(0, 0, KeyBoardUtil.getFontHeight(tv), KeyBoardUtil.getFontHeight(tv));
                EmojiSpan span = new EmojiSpan(drawable);
                sb.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb;
    }

    /**
     * 转换emoji表情，带草稿前缀
     *
     * @param context
     * @param content
     * @return
     */
    public static SpannableStringBuilder convertDraft(Context context, String content, TextView tv) {
        SpannableStringBuilder sb = convert(context, content, tv);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        sb.setSpan(redSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * 插入emoji
     *
     * @param text
     */
    public static void insert(EditText editText, CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((editText.getText()));
        int iCursorEnd = Selection.getSelectionEnd((editText.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) editText.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((editText.getText()));
        ((Editable) editText.getText()).insert(iCursor, text);
    }
}
