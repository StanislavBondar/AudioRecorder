package com.testapp.stas.audiorecorder.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Called to read & play audio from file
 * */

public class AudioPlayService extends Service {
    public AudioPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new AsyncPlayer().execute();

        stopSelf();

        return Service.START_NOT_STICKY;
    }

    protected class AsyncPlayer extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            int mFrequency = 11025,mChannelConfiguration = AudioFormat.CHANNEL_OUT_MONO;
            int mAudioEncoding = AudioFormat.ENCODING_PCM_16BIT;

            File mFile = new File(Environment.getExternalStorageDirectory(), "test.pcm");

            int mBufferSize = AudioTrack.getMinBufferSize(mFrequency, mChannelConfiguration, mAudioEncoding);

            int mShortSizeInBytes = Short.SIZE / Byte.SIZE;

            int mBufferSizeInBytes = (int) (mFile.length() / mShortSizeInBytes);

            short[] mAudioData = new short[mBufferSizeInBytes];

            try {
                InputStream mInputStream = new FileInputStream(mFile);
                BufferedInputStream mBufferedInputStream = new BufferedInputStream(mInputStream);
                DataInputStream mDataInputStream = new DataInputStream(mBufferedInputStream);

                int i = 0;

                while (mDataInputStream.available() > 0) {
                    mAudioData[i] = mDataInputStream.readShort();
                    i++;
                }

                mDataInputStream.close();

                AudioTrack mAudioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        mFrequency,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        mBufferSize,
                        AudioTrack.MODE_STREAM);

                short[] mAudioDataConverted = new short[mAudioData.length];

                for (int j = mAudioDataConverted.length-1, y = 0; j != 0; j--, y++) {

                    mAudioDataConverted[j] = mAudioData[y];

                }

                mAudioTrack.play();
                mAudioTrack.write(mAudioDataConverted, 0, mBufferSizeInBytes);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
