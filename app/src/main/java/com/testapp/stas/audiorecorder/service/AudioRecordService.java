package com.testapp.stas.audiorecorder.service;

import android.app.Service;

import android.content.Intent;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;

import android.util.Log;

import com.testapp.stas.audiorecorder.MainActivity;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * Called to recording audio from mic & save to file
 * */
public class AudioRecordService extends Service {

    private boolean mRecording = false;
    private String TAG = MainActivity.TAG;


    public AudioRecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final AsyncTask asyncTask = new AsyncRecorder();

        new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                if (asyncTask.getStatus() != AsyncTask.Status.RUNNING) {

                    asyncTask.execute();

                }

                Log.i(TAG, "Left " + millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                mRecording = false;
                Log.i(TAG, "FINISH");

            }
        }.start();

        stopSelf();

        return Service.START_NOT_STICKY;
    }

    protected class AsyncRecorder extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            mRecording = true;
            File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");
            int mSampleRate = 11025;

            try {

                Log.i(TAG, "FILE IS =====" + file.toString());

                OutputStream mOutputStream = new FileOutputStream(file);
                BufferedOutputStream mBufferedOutputStream = new BufferedOutputStream(mOutputStream);
                DataOutputStream mDataOutputStream = new DataOutputStream(mBufferedOutputStream);

                int mMinBufferSize = AudioRecord.getMinBufferSize(mSampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                short[] mAudioData = new short[mMinBufferSize];

                AudioRecord mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        mSampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        mMinBufferSize);

                mAudioRecord.startRecording();

                while (mRecording) {

                    int mNumberOfShort = mAudioRecord.read(mAudioData, 0, mMinBufferSize);
                    for (int i = 0; i < mNumberOfShort; i++) {

                        mDataOutputStream.writeShort(mAudioData[i]);

                    }
                }

                mAudioRecord.stop();
                Log.i(TAG, "AudioRECORD ======================= STOP");
                mDataOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i(TAG, "AsyncTask ======================= STOP");
        }
    }

}
