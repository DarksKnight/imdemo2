/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.GF.platform.uikit.util.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class GFAudioDecoder {
    private static final int TIMEOUT_US = 1000;
    private MediaExtractor mExtractor;
    private MediaCodec mDecoder;
    private Thread mThread;
    private boolean mRunning;
    private int mSampleRate = 0;
    private int mVolume;
    private GFAudioListener listener = null;

    private static GFAudioDecoder single = null;

    private GFAudioDecoder() {
    }

    public static synchronized GFAudioDecoder getDefault() {
        if (single == null) {
            single = new GFAudioDecoder();
        }
        return single;
    }

    public void start(String inFilePath) {
        mExtractor = new MediaExtractor();
        try {
            mExtractor.setDataSource(inFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MediaFormat mediaFormat = mExtractor.getTrackFormat(0);
        if (mediaFormat == null) {
            return;
        }

        String mediaType = "audio/amr-wb";
        String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
        if (mediaType.equals(mime)) {
            mExtractor.selectTrack(0);
            mSampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        }

        try {
            mDecoder = MediaCodec.createDecoderByType(mediaType);
            mDecoder.configure(mediaFormat, null, null, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mDecoder == null) {
            return;
        }

        mDecoder.start();

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    decodeAndPlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "AudioDecoder Thread");

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

    private void decodeAndPlay() {
        int buffsize = AudioTrack.getMinBufferSize(mSampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        // create an audiotrack object
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                buffsize,
                AudioTrack.MODE_STREAM);

        audioTrack.play();

        while (mRunning) {
            ByteBuffer[] inputBuffers = mDecoder.getInputBuffers();
            ByteBuffer[] outputBuffers = mDecoder.getOutputBuffers();

            int inIndex = mDecoder.dequeueInputBuffer(TIMEOUT_US);

            if (inIndex >= 0) {
                ByteBuffer buffer = inputBuffers[inIndex];
                int sampleSize = mExtractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    // We shouldn't stop the playback at this point, just pass the EOS
                    // flag to mDecoder, we will get it again from the
                    // dequeueOutputBuffer
                    Log.d("AudioDecoder", "InputBuffer BUFFER_FLAG_END_OF_STREAM");
                    mDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);

                } else {
                    mDecoder.queueInputBuffer(inIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
                    mExtractor.advance();
                }

                BufferInfo info = new BufferInfo();
                int outIndex = mDecoder.dequeueOutputBuffer(info, TIMEOUT_US);

                while (outIndex >= 0) {
                    ByteBuffer outBuffer = outputBuffers[outIndex];

                    final byte[] chunk = new byte[info.size];
                    outBuffer.get(chunk); // Read the buffer all at once
                    outBuffer.clear(); // ** MUST DO!!! OTHERWISE THE NEXT TIME YOU GET THIS SAME BUFFER BAD THINGS WILL HAPPEN

                    if (info.size > 0) {
                        mVolume = Math.abs((short) (((chunk[1] << 8) | chunk[0] & 0xff))) / 328;
                        Log.i("BJMEngine", "decoder mVolume = " + mVolume);
                        listener.record(mVolume);
                    }

                    audioTrack.write(chunk, info.offset, info.offset + info.size); // AudioTrack write data
                    mDecoder.releaseOutputBuffer(outIndex, false);

                    outIndex = mDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
                }

                // All decoded frames have been rendered, we can stop playing now
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.i("AudioEncoder", "Playing got end of stream");
                    break;
                }
            }
        }

        try {
            mVolume = 0;
            mRunning = false;

            mDecoder.stop();
            mDecoder.release();
            mDecoder = null;

            mExtractor.release();
            mExtractor = null;

            audioTrack.stop();
            audioTrack.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("AudioEncoder", "Play end");
    }

    public void setListener(GFAudioListener listener) {
        this.listener = listener;
    }
}