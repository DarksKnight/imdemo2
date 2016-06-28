package com.GF.platform.uikit.util.audio;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GFAudioEncoder {
    private static final int TIMEOUT_US = 1000;

    private MediaCodec mEncoder;
    private BufferedOutputStream mOutputStream;

    private AudioRecord mAudioRecord;
    private Thread mThread;
    private int mBufferSize;
    private int mVolume;
    private boolean mRunning;

    private GFAudioListener listener = null;

    private static GFAudioEncoder single = null;

    private GFAudioEncoder() {
    }

    public static synchronized GFAudioEncoder getDefault() {
        if (single == null) {
            single = new GFAudioEncoder();
        }
        return single;
    }

    public void start(String outFilePath) {
        mBufferSize = AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mBufferSize);

        File file = new File(outFilePath);

        writeHeader(file);

        try {
            mOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String mediaType = "audio/amr-wb";

            mEncoder = mEncoder.createEncoderByType(mediaType);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(mediaType, 16000, 1);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 23850);
            mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, mBufferSize);

            mEncoder.configure(mediaFormat, null, null, mEncoder.CONFIGURE_FLAG_ENCODE);
            mEncoder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mAudioRecord.startRecording();

        mThread = new Thread(new Runnable() {
            public void run() {
                recordAndEncode();
            }
        }, "AudioEncoder Thread");

        mRunning = true;
        mVolume = 0;

        mThread.start();
    }

    public boolean isRunning() {
        return mRunning && mThread != null;
    }

    public int getVolume() {
        return mVolume;
    }

    private void writeHeader(File f) {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fs = new FileOutputStream(f);
            byte[] bs = "#!AMR-WB\n".getBytes();
            fs.write(bs, 0, bs.length);
            fs.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mRunning = false;

        if (mThread != null) {
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mThread = null;
    }

    public void recordAndEncode() {
        byte[] buffer = new byte[mBufferSize];

        while (mRunning) {
            int bytes = mAudioRecord.read(buffer, 0, mBufferSize);
            if (bytes < 0) {
                break;
            }

            if (bytes > 0) {
                mVolume = Math.abs((short) (((buffer[1] << 8) | buffer[0] & 0xff))) / 328;
                listener.record(mVolume);
            }

            Log.i("BJMEngine", "encoder mVolume = " + mVolume);

            ByteBuffer[] inputBuffers = mEncoder.getInputBuffers();
            ByteBuffer[] outputBuffers = mEncoder.getOutputBuffers();

            int inputBufferIndex = mEncoder.dequeueInputBuffer(TIMEOUT_US);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();

                inputBuffer.put(buffer);

                mEncoder.queueInputBuffer(inputBufferIndex, 0, buffer.length, System.currentTimeMillis() * 1000, 0);
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outIndex = mEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);

            while (outIndex >= 0) {
                ByteBuffer outputBuffer = outputBuffers[outIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);

                /**
                 * FrameHeader
                 *
                 *  0  1  2  3  4  5  6  7
                 *  P  |---FT---|  Q  P  P
                 *
                 *  P:  0
                 *  FT: FrameType
                 *  Q:  0 Bad frame
                 *
                 */
                if ((outData[0] & 4) == 0) {
                    Log.e("AudioEncoder", "Got bad frame, skipping...");

                } else {
                    try {
                        mOutputStream.write(outData, 0, outData.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mEncoder.releaseOutputBuffer(outIndex, false);
                outIndex = mEncoder.dequeueOutputBuffer(bufferInfo, 0);
            }
        }

        try {
            mVolume = 0;

            mRunning = false;
            mEncoder.stop();
            mEncoder.release();

            mOutputStream.flush();
            mOutputStream.close();

            mAudioRecord.stop();
            mAudioRecord.release();

            mAudioRecord = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("AudioEncoder", "Record end");
    }

    public void setListener(GFAudioListener listener) {
        this.listener = listener;
    }
}