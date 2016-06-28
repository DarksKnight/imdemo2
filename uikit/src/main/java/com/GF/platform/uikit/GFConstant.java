package com.GF.platform.uikit;

import android.Manifest;
import android.os.Environment;

/**
 * Created by sunhaoyang on 2016/6/3.
 */

public class GFConstant {

    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    public static final String[] PHOTO_SELECT_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] AUDIO_PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int PERMISSION_REQUEST_CODE = 0;

    public static final int MSG_TYPE_EXPRESSION = 0;
    public static final int MSG_TYPE_TEXT = 1;
    public static final int MSG_TYPE_AUDIO = 2;
    public static final int MSG_TYPE_PIC = 3;

    public static final String EVENT_SEND_MESSAGE = "EVENT_SEND_MESSAGE";
    public static final String EVENT_FUNCTION_SELECTED = "EVENT_FUNCTION_SELECTED";
    public static final String EVENT_DELETE_MESSAGE = "EVENT_DELETE_MESSAGE";
    public static final String EVENT_UI = "EVENT_UI";
    public static final String EVENT_IMAGE_SELECT = "EVENT_IMAGE_SELECT";
    public static final String EVENT_MESSAGE_STATUS = "EVENT_MESSAGE_STATUS";
    public static final String EVENT_PERMISSION = "EVENT_PERMISSION";

    public static final int AUDIO_STATUS_DEFAULT = 0;
    public static final int AUDIO_STATUS_CANCEL = 1;
    public static final int AUDIO_STATUS_PLAY = 2;

    public static String AUDIO_OUT_PATH = Environment.getExternalStorageDirectory() + "/Download/" + System.currentTimeMillis() / 1000 + ".amr";
}
