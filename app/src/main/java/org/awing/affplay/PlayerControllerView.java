package org.awing.affplay;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.awing.affplay.util.DisplayUtils;

public class PlayerControllerView extends LinearLayout implements View.OnClickListener , TouchSlideManager.OnSlideListener{

    private static final String TAG = "PlayerControllerView";

    private ImageView mImgCancel;
    private ImageView mImgPlayPause;
    private TextView mTvDuration;
    private int mPlayPauseRes = -1;
    private static final int SEEK_SPEED_PER_DP = 100; // ms
    private int mSeekStartMs = -1;
    private int mSeekToMs = -1;
    private int mDuration = -1;
    private Ffplayer mPlayer;

    public PlayerControllerView(Context context) {
        super(context);
        initView();
    }

    public PlayerControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public PlayerControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.player_control_pannel, this, true);
        mImgCancel = (ImageView) findViewById(R.id.img_cancel);
        mImgPlayPause = (ImageView) findViewById(R.id.img_play_pause);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);
        mImgPlayPause.setOnClickListener(this);
        mImgCancel.setOnClickListener(this);
        mPlayer = Ffplayer.getInstance(getContext());
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "OnClicke");
        if (mImgPlayPause == v) {
            mPlayer.togglePause();
            if (mPlayPauseRes == -1) {
                int playerState = mPlayer.getPlayState();
                if (playerState == Ffplayer.STATE_PLAYING) {
                    mPlayPauseRes = R.drawable.ic_player_pause;
                } else {
                    mPlayPauseRes = R.drawable.ic_player_play;
                }
            }
            if (mPlayPauseRes == R.drawable.ic_player_pause) {
                mPlayPauseRes = R.drawable.ic_player_play;
            } else {
                mPlayPauseRes = R.drawable.ic_player_pause;
            }
            mImgPlayPause.setImageResource(mPlayPauseRes);
        } else if (mImgCancel == v) {
            mPlayer.stop();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            mTvDuration.setText(mPlayer.getDurationString());
            int playerState = mPlayer.getPlayState();
            if (playerState == Ffplayer.STATE_PLAYING) {
                mPlayPauseRes = R.drawable.ic_player_pause;
            } else {
                mPlayPauseRes = R.drawable.ic_player_play;
            }
            mImgPlayPause.setImageResource(mPlayPauseRes);
        }
    }

    @Override
    public void onSlideStart(PointF startPosition) {
        mSeekStartMs = mPlayer.getPlayPosition();
        mSeekToMs = mSeekStartMs;
        mDuration = mPlayer.getDuration();
        Log.d(TAG, "onSlideStart mSeekStartMs:" + mSeekStartMs + "[" + mPlayer.formatTimeString(mSeekStartMs) + "] mDuration:" + mDuration);
    }

    @Override
    public void onSlideTo(PointF from, PointF to) {
        int movePx = (int)(to.x - from.x);
        int moveDp = DisplayUtils.px2dp(movePx, getContext());
        int moveMs = moveDp * SEEK_SPEED_PER_DP;
        mSeekToMs += moveMs;
        if (mSeekToMs < 0) {
            mSeekToMs = 0;
        } else if (mDuration != -1 && mSeekToMs > mDuration) {
            mSeekToMs = mDuration;
        }
        Log.d(TAG, "onSlideTo mSeekToMs:" + mSeekToMs + "[" + mPlayer.formatTimeString(mSeekToMs) + "]");
    }

    @Override
    public void onSlideEnd(PointF endPosition) {
        Log.d(TAG, "onSlideEnd mSeekStartMs:" + mSeekStartMs + " mSeekToMs:" + mSeekToMs + "[" + mPlayer.formatTimeString(mSeekToMs) + "]");
        if (mSeekToMs != mSeekStartMs) {
            mPlayer.seekTo(mSeekToMs);
        }
        mSeekStartMs = -1;
        mSeekToMs = -1;
        mDuration = -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent event:" + event);
        return super.onTouchEvent(event);
    }
}
