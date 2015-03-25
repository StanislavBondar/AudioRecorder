package com.testapp.stas.audiorecorder;


import android.app.Activity;

import android.content.Intent;

import android.os.CountDownTimer;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.testapp.stas.audiorecorder.service.AudioPlayService;
import com.testapp.stas.audiorecorder.service.AudioRecordService;

public class MainActivity extends Activity {

    public static final String TAG = "MyLog";
    private ImageView mButtonRecord, mButtonPlay;
    private TextView mCountDownText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonRecord = (ImageView) findViewById(R.id.recordButton);
        mButtonPlay = (ImageView) findViewById(R.id.playButton);
        mCountDownText = (TextView) findViewById(R.id.timer);
        mCountDownText.setText("00:00:000");
        View.OnClickListener onClickListener = new OnClickListener();

        mButtonRecord.setOnClickListener(onClickListener);
        mButtonPlay.setOnClickListener(onClickListener);

    }

    private class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.recordButton:

                    startService(new Intent(MainActivity.this, AudioRecordService.class));

                    startCountdown();

                    mButtonRecord.setImageDrawable(getResources().getDrawable(R.drawable.record_pressed));
                    mButtonRecord.setClickable(false);
                    mButtonPlay.setClickable(false);

                    break;
                case R.id.playButton:

                    startService(new Intent(MainActivity.this, AudioPlayService.class));
                    startCountdown();
                    mButtonRecord.setClickable(false);
                    mButtonPlay.setImageDrawable(getResources().getDrawable(R.drawable.play_pressed));
                    mButtonPlay.setClickable(false);

                    break;
            }

        }
    }

    protected void startCountdown() {

        new CountDownTimer(10000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {

                String mMilliSeconds = String.valueOf(millisUntilFinished);

                mCountDownText.setText("00:0" + millisUntilFinished/1000+":"+ mMilliSeconds.substring(1));

                if (millisUntilFinished < 1000) {
                    mCountDownText.setText("00:0" + millisUntilFinished/1000+":0"+ mMilliSeconds.substring(1));
                }

                if (millisUntilFinished < 100) {

                    mCountDownText.setText("00:0" + millisUntilFinished/1000+":00"+ mMilliSeconds.substring(1));
                }

            }

            @Override
            public void onFinish() {
                mCountDownText.setText("00:00:000");
                mButtonRecord.setClickable(true);
                mButtonRecord.setImageDrawable(getResources().getDrawable(R.drawable.record_normal));
                mButtonPlay.setClickable(true);
                mButtonPlay.setImageDrawable(getResources().getDrawable(R.drawable.play_normal));

            }
        }.start();

    }


}
