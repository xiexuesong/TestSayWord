package util;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * @Author: savion
 * @Date: 2018/12/12 17:04
 * @Des:
 **/
public class RecordUtil {
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private RecordingListener recordingListener;
    private CountDownTimer countDownTimer;
    private long maxSecond = 60 * 1000;
    public static RecordUtil instance;
    private static Object lock = new Object();
    private String currentAudioFile;

    private static final int STATE_RUNNING = 1;
    private static final int STATE_START = 2;
    private static final int STATE_END = 3;
    private static final int STATE_ERROR = 4;
    private static final int STATE_PAUSE = 5;

    private RecordUtil() {
    }

    public static RecordUtil getInstance() {
        synchronized (lock) {
            if (instance == null)
                instance = new RecordUtil();
        }
        return instance;
    }

    public void setMaxRecordTime(int sec) {
        this.maxSecond = sec * 1000l;
    }

    public void setMaxRecordTime(long mill) {
        this.maxSecond = mill;
    }

    public void setRecordingListener(RecordingListener recordingListener) {
        this.recordingListener = recordingListener;
    }

    private void initMedia() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(16000);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        String parentPath = FileUtil.getRecordPath();
        File parent = new File(parentPath);
        if (parent==null||!parent.exists()){
            parent.mkdirs();
        }
        File[] files = parent.listFiles();
        for (int i=0;files!=null&&i<files.length;i++){
            files[i].delete();
        }
        Log.e("debug","共删除:"+parentPath+"目录下"+(files!=null?files.length:0)+"个缓存文件");
        StringBuilder stringBuilder = new StringBuilder(FileUtil.getRecordPath());
        stringBuilder.append(File.separator);
        stringBuilder.append(String.valueOf(System.currentTimeMillis()));
        stringBuilder.append(".aac");
        File file = new File(stringBuilder.toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        currentAudioFile = file.getAbsolutePath();
        mediaRecorder.setOutputFile(stringBuilder.toString());
    }

    private void initCount() {
        countDownTimer = new CountDownTimer(maxSecond, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                long current = (int) (maxSecond - millisUntilFinished);
                double volume = 0d;
                if (mediaRecorder != null) {
                    double maxAmplitude = (double) mediaRecorder.getMaxAmplitude();
                    Double.isNaN(maxAmplitude);
                    maxAmplitude /= 100.0d;
                    if (maxAmplitude > 1.0d) {
                        volume = 20.0d * Math.log10(maxAmplitude);
                    }
                } else {
                    stop();
                }
//                if (recordingListener != null) {
//                    recordingListener.onSoundRecordingProgress(current, volume);
//                }
                Bundle bundle = new Bundle();
                bundle.putLong("second",current);
                bundle.putDouble("volume",volume);
                Message message = handler.obtainMessage(STATE_RUNNING);
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFinish() {
                stop();
            }
        };
    }


    public boolean start() {
        try {
            if (mediaRecorder == null)
                initMedia();
            if (countDownTimer == null)
                initCount();
            else
                countDownTimer.cancel();
            mediaRecorder.prepare();
            mediaRecorder.start();
            countDownTimer.start();
            isRecording = true;

//            if (recordingListener != null)
//                recordingListener.onSoundRecordingStart();
            handler.sendMessage(handler.obtainMessage(STATE_START));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            stop();
            Bundle bundle = new Bundle();
            bundle.putString("error",e.getMessage());
            Message message = handler.obtainMessage(STATE_ERROR);
            message.setData(bundle);
            handler.sendMessage(message);
//            if (recordingListener != null)
//                recordingListener.onSoundRecordingError(e);
            return false;
        }
    }

    public void pause() {
        if (mediaRecorder != null && isRecording)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.pause();
                handler.sendMessage(handler.obtainMessage(STATE_PAUSE));
            } else {
                stop();
            }
    }

    public void resume() {
        if (mediaRecorder != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.resume();
            } else {
                start();
            }
    }

    public boolean isRecording() {
        return isRecording && mediaRecorder != null;
    }

    public void stop() {
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
//        if (handler!=null){
//            handler.removeMessages(STATE_END);
//            handler.removeMessages(STATE_START);
//            handler.removeMessages(STATE_RUNNING);
//            handler.removeMessages(STATE_ERROR);
//        }
        Bundle bundle = new Bundle();
        File file = new File(currentAudioFile);
        String name = "";
        String path = "";
        if (file!=null&&file.exists()) {
            name = file.getName();
            path = file.getParentFile().getAbsolutePath();
        }
        bundle.putString("file",path);
        bundle.putString("name",name);
        Message message = handler.obtainMessage(STATE_END);
        message.setData(bundle);
        handler.sendMessage(message);

        countDownTimer = null;
        mediaRecorder = null;
        isRecording = false;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RecordUtil.STATE_END:
                    if (recordingListener!=null) {
                        Bundle bundle = msg.getData();
                        String path = bundle.getString("file","");
                        String name = bundle.getString("name","");
                        recordingListener.onSoundRecordingEnd(path,name);
                    }
                    if (handler!=null){
                        handler.removeMessages(STATE_END);
                        handler.removeMessages(STATE_START);
                        handler.removeMessages(STATE_RUNNING);
                        handler.removeMessages(STATE_ERROR);
                    }
                    break;
                case RecordUtil.STATE_ERROR:
                    if (recordingListener!=null){
                        String message =  msg.obj!=null?msg.obj.toString():"unknown exception";
                        recordingListener.onSoundRecordingError(new RecordingExceptoin(message));
                    }
                    break;
                case RecordUtil.STATE_RUNNING:
                    if (recordingListener!=null) {
                        Bundle bundle = msg.getData();
                        long sec = bundle.getLong("second",0);
                        double volume = bundle.getDouble("volume",0);
                        recordingListener.onSoundRecordingProgress(sec,volume);
                    }
                    break;
                case RecordUtil.STATE_START:
                    if (recordingListener != null)
                        recordingListener.onSoundRecordingStart();
                    break;
                case RecordUtil.STATE_PAUSE:
                    if (recordingListener!=null)
                        recordingListener.onSoundRecordingPause(0);
                    break;
            }
        }
    };
    public static class RecordingExceptoin extends RuntimeException {
        public RecordingExceptoin(String msg){
            super(msg);
        }
    }

}
