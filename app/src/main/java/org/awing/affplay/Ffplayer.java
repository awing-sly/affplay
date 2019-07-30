package org.awing.affplay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class Ffplayer {
    private static final String TAG = "Ffplayer";

    public static final int STATE_STOPPED = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSED = 3;

    public static final int EVENT_STATE_CHANGED = 10;

    private Context mContext;
    private String mMediaPath;
    private OnStateChangedListener mStateListener;

    public interface OnStateChangedListener{
        void onStateChanged(int oldState, int newState);
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        mStateListener = listener;
    }

    private void dispatchStateChangedListener(int oldState, int newState) {
        if (mStateListener != null) {
            mStateListener.onStateChanged(oldState, newState);
        }
    }

    static {
        System.loadLibrary("ffplay-lib");
    }

    private static Ffplayer sInstance;
    public static synchronized Ffplayer getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Ffplayer(context);
        }
        return sInstance;
    }

    private Ffplayer(Context ctx) {
        mContext = ctx;
        nativeInit();
    }

    public void startPlayActivity(String path) {
        Intent intent = new Intent(mContext, FfplaySdlActivity.class);
        setDataSource(path);
        mContext.startActivity(intent);
    }

    private void notifyEventFromNative(int msg, int ext1, int ext2, Object obj) {
        Log.d(TAG, "notifyEventFromNative msg:" + msg + " ext1:" + ext1  + " ext2:" + ext2);
        switch (msg) {
            case EVENT_STATE_CHANGED:
                dispatchStateChangedListener(ext1, ext2);
                break;
            default:
                Log.d(TAG, "notifyEventFromNative Unknown event:" + msg);
        }
    }

    private native void nativeInit() ;
    private native void nativeDeinit();
    public native int getDuration();
    public native void togglePause();
    public native void start(String path);
    public native void stop();
    public native void seekTo(int msec);
    public native int getPlayState();
    public native int getPlayPosition();

    public void start() {
        if (TextUtils.isEmpty(mMediaPath)) {
            throw new IllegalArgumentException("No data source was set");
        }
        int state = getPlayState();
        if (state == STATE_STOPPED) {
            start(mMediaPath);
        }
    }

    public void setDataSource(String path) {
        mMediaPath = path;
    }

    public String getMediaPath() {
        return mMediaPath;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        nativeDeinit();
    }

    public String getDurationString() {
        String result = "-:-:-";
        int dur = getDuration();
        Log.d(TAG, "Duration:" + dur);
        if (dur > 0) {
            return formatTimeString(dur);
        }
        return result;
    }

    public String formatTimeString(int timeMs) {
        String result = "-:-:-";
        if (timeMs >= 0) {
            int totalSec = timeMs / 1000;
            int sec = totalSec % 60;
            int min = totalSec / 60 % 60;
            int hour = totalSec / 3600;
            result = hour + ":" + min + ":" + sec;
        }
        return result;
    }
}
