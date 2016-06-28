package com.GF.platform.uikit.widget.audiopop;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.R;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.event.GFEventDispatch;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.util.audio.GFAudioDecoder;
import com.GF.platform.uikit.util.audio.GFAudioEncoder;
import com.GF.platform.uikit.util.audio.GFAudioListener;
import com.GF.platform.uikit.widget.audioview.GFAudioRecordView;
import com.GF.platform.uikit.widget.progressbar.GFCircleProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 录音悬浮层
 * Created by sunhaoyang on 2016/6/14.
 */

public class GFAudioRecordPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context = null;
    private View view = null;
    private ImageView ivDel = null;
    private ImageView ivPlay = null;
    private TextView tvInfo = null;
    private TextView tvTime = null;
    private LinearLayout llNormal = null;
    private ViewStub vsReplay = null;
    private int delX, delY, delWidth, delHeight;
    private int playX, playY, playWidth, playHeight;
    private GFAudioRecordView GFAudioRecordViewLeft = null;
    private GFAudioRecordView GFAudioRecordViewRight = null;
    private int status = GFConstant.AUDIO_STATUS_DEFAULT;
    private Button btnDetailDel = null;
    private Button btnDetailSend = null;
    private GFCircleProgressBar btnDetailPlay = null;
    private boolean isPlaying = false;
    private final Random r = new Random();
    private final ValueAnimator animator = ValueAnimator.ofInt(0, 100);
    private Timer timer = null;
    private String audioPath = "";
    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int progress = (int) animation.getAnimatedValue();
            btnDetailPlay.setProgress(progress);
            if (progress == 100) {
                btnDetailPlay.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_play);
                btnDetailPlay.setProgress(0);
                GFAudioRecordViewLeft.reload();
                GFAudioRecordViewRight.reload();
                isPlaying = false;
                timer.cancel();
            }
        }
    };
    private Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            double sign = GFUtil.volumnConvert(msg.what);
            GFAudioRecordViewLeft.setVolumn((int) sign);
            GFAudioRecordViewRight.setVolumn((int) sign);
        }
    };
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String minute = time / 60 + "";
            String second = time % 60 < 10 ? "0" + time % 60 : time % 60 + "";
            tvTime.setText(minute + ":" + second);
        }
    };
    private int time = 0;
    private TimerTask task = null;

    public GFAudioRecordPopupWindow(Context context, int keyBoardHeight, String path) {
        this.audioPath = path;
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bjmgf_message_chat_audio_record_view, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        int height = 0;
        //安卓4.4以上系统，需要获取状态栏高度去除
        if (Build.VERSION.SDK_INT > 21) {
            height = GFUtil.getStatusHeight(context);
        }
        setHeight(GFUtil.getScreenHeight(context) - keyBoardHeight - height);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());

        ivDel = (ImageView) view.findViewById(R.id.bjmgf_message_chat_audio_record_del_iv);
        ivPlay = (ImageView) view.findViewById(R.id.bjmgf_message_chat_audio_record_play_iv);
        tvInfo = (TextView) view.findViewById(R.id.bjmgf_message_chat_audio_record_text);
        GFAudioRecordViewLeft = (GFAudioRecordView) view.findViewById(R.id.bjmgf_message_chat_audio_view_left);
        GFAudioRecordViewRight = (GFAudioRecordView) view.findViewById(R.id.bjmgf_message_chat_audio_view_right);
        llNormal = (LinearLayout) view.findViewById(R.id.bjmgf_message_chat_audio_record_ll);
        vsReplay = (ViewStub) view.findViewById(R.id.bjmgf_message_chat_audio_record_replay_vs);
        tvTime = (TextView) view.findViewById(R.id.bjmgf_message_chat_audio_time_tv);
        startTimer();
        GFAudioEncoder.getDefault().setListener(new GFAudioListener() {
            @Override
            public void record(int volumn) {
                viewHandler.sendEmptyMessage(volumn);
            }
        });
        GFAudioEncoder.getDefault().start(audioPath);
    }

    public void startTimer() {
        timer = new Timer(true);
        task = new TimerTask() {
            public void run() {
                time++;
                timeHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    public void initViewLocation() {
        //删除按钮
        delWidth = ivDel.getMeasuredWidth();
        delHeight = ivDel.getMeasuredHeight();
        int[] delPosition = new int[2];
        ivDel.getLocationOnScreen(delPosition);
        delX = delPosition[0];
        delY = delPosition[1];

        //播放按钮
        playWidth = ivPlay.getMeasuredWidth();
        playHeight = ivPlay.getMeasuredHeight();
        int[] playPosition = new int[2];
        ivPlay.getLocationOnScreen(playPosition);
        playX = playPosition[0];
        playY = playPosition[1];
    }

    public void showAtLocation() {
        super.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.TOP, 0, 0);
    }

    public void reset() {
        tvInfo.setText("");
        ivDel.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_del);
        ivPlay.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_play);
    }

    public void setPointLocation(float x, float y) {
        int half = GFUtil.getScreenWidth(context) / 2;
        if (x < half) {
            if ((x > delX && x < delX + delWidth) && (y > delY && y < delY + delHeight)) {
                ivDel.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_del_selected);
                tvInfo.setText(context.getResources().getText(R.string.bjmgf_message_chat_up_audio_record_cancel));
                status = GFConstant.AUDIO_STATUS_CANCEL;
            } else {
                ivDel.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_del);
                tvInfo.setText("");
                status = GFConstant.AUDIO_STATUS_DEFAULT;
            }
        } else {
            if ((x > playX && x < playX + playWidth) && (y > playY && y < playY + playHeight)) {
                ivPlay.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_play_selected);
                tvInfo.setText(context.getResources().getText(R.string.bjmgf_message_chat_up_audio_record_play));
                status = GFConstant.AUDIO_STATUS_PLAY;
            } else {
                ivPlay.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_play);
                tvInfo.setText("");
                status = GFConstant.AUDIO_STATUS_DEFAULT;
            }
        }
    }

    public GFMessage getMessage() {
        GFMessage gfMessage = new GFMessage("一般的帅", "", GFUtil.getDate(), "", GFMessage.Category.NORMAL_ME, null, false);
        gfMessage.setSending(true);
        gfMessage.setAudioTime(audioTime());
        gfMessage.setAudioPath(audioPath);
        gfMessage.setMsgId(System.currentTimeMillis() / 1000 + "");
        return gfMessage;
    }

    public int getStatus() {
        return status;
    }

    public void showReplay() {
        tvInfo.setText("");
        llNormal.setVisibility(View.GONE);
        vsReplay.inflate();
        update(getWidth(), GFUtil.getScreenHeight(context));

        btnDetailDel = (Button) view.findViewById(R.id.bjmgf_message_chat_audio_record_detail_del);
        btnDetailSend = (Button) view.findViewById(R.id.bjmgf_message_chat_audio_record_detail_send);
        btnDetailPlay = (GFCircleProgressBar) view.findViewById(R.id.bjmgf_message_chat_audio_record_detail_play);

        btnDetailDel.setOnClickListener(this);
        btnDetailSend.setOnClickListener(this);
        btnDetailPlay.setOnClickListener(this);

        play();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bjmgf_message_chat_audio_record_detail_del) {
            animator.end();
            dismiss();
        } else if (v.getId() == R.id.bjmgf_message_chat_audio_record_detail_send) {
            animator.end();
            GFEventDispatch.post(GFConstant.EVENT_SEND_MESSAGE, getMessage());
            dismiss();
        } else if (v.getId() == R.id.bjmgf_message_chat_audio_record_detail_play) {
            if (isPlaying) {
                animator.end();
                btnDetailPlay.setProgress(0);
            }
            time = 0;
            reload();
            play();
        }
    }

    private void play() {
        timer = new Timer(true);
        task = new TimerTask() {
            public void run() {
                time++;
                timeHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 1000, 1000);
        isPlaying = true;
        btnDetailPlay.setBackgroundResource(R.mipmap.bjmgf_message_chat_audio_record_detail_stop);
        GFAudioDecoder.getDefault().setListener(new GFAudioListener() {
            @Override
            public void record(int volumn) {
                viewHandler.sendEmptyMessage(volumn);
            }
        });
        GFAudioDecoder.getDefault().start(audioPath);
        animator.addUpdateListener(listener);
        animator.setDuration(getMessage().getAudioTime());
        animator.start();
    }

    public long audioTime() {
        long size = 0l;
        try {
            File file = new File(audioPath);
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size / 61 * 20;
    }

    public void stopTimer() {
        time = 0;
        reload();
    }

    private void reload() {
        if (GFAudioEncoder.getDefault().isRunning() || GFAudioDecoder.getDefault().isRunning()) {
            GFAudioEncoder.getDefault().stop();
            GFAudioDecoder.getDefault().stop();
        }
        timer.cancel();
        task.cancel();
        timer = null;
        task = null;
        tvTime.setText("0:00");
    }
}
