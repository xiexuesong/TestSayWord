package com.wangzhen.admin.testsayword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import util.RecordUtil;
import util.RecordingListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Button button;
    private RecordUtil recordUtil ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button_stop);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                startRecord();
                break;
            case R.id.button_stop:
                stopRecord();
                break;
            default:
                break;
        }
    }

    /**
     * 停止录音
     */
    private void stopRecord() {
        if(recordUtil != null){
            recordUtil.stop();
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {

        recordUtil = RecordUtil.getInstance();
        recordUtil.setMaxRecordTime(60);
        recordUtil.setRecordingListener(new RecordingListener() {
            @Override
            public void onSoundRecordingEnd(String path, String name) {
                Log.i("MDL","录音结束:" + path + " name:" + name);
            }

            @Override
            public void onSoundRecordingProgress(long sec, double volume) {
                Log.i("MDL","时长:" + sec + " 音量:" + volume);
            }

            @Override
            public void onSoundRecordingStart() {
                Log.i("MDL","开始录音");
            }

            @Override
            public void onSoundRecordingError(Throwable throwable) {
                Log.i("MDL","录音出错:" + throwable.getMessage());
            }

            @Override
            public void onSoundRecordingPause(long current) {
                Log.i("MDL","录音暂停:" + current);
            }
        });
        recordUtil.start();


    }
}
