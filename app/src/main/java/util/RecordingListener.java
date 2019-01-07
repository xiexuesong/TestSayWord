package util;

/**
 * @Author: savion
 * @Date: 2018/12/7 19:54
 * @Des:
 **/
public interface RecordingListener {
    void onSoundRecordingEnd(String path, String name);

    void onSoundRecordingProgress(long sec, double volume);

    void onSoundRecordingStart();

    void onSoundRecordingError(Throwable throwable);

    void onSoundRecordingPause(long current);
}
