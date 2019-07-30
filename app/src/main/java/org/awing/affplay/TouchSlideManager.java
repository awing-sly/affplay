package org.awing.affplay;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

public class TouchSlideManager {
    public interface OnSlideListener{
        void onSlideStart(PointF startPosition);
        void onSlideTo(PointF from, PointF to);
        void onSlideEnd(PointF endPosition);
    }
    private static final String TAG = "TouchSlideManager";

    private OnSlideListener mListener;
    // map of pointId to point Position
    private Map<Integer, PointF> mLastFingerPositions = new HashMap<Integer, PointF>();
    private int mMasterPointerId;
    public void setOnSlideListener(OnSlideListener listener) {
        mListener = listener;
    }

    private void reset() {
        mMasterPointerId = -1;
        mLastFingerPositions.clear();
    }

    private PointF updateLastPointer(MotionEvent event, int pointerIndex, boolean updateMaster) {
        float x;
        float y;
        int pointerId = 0;
        int pointerCount = 0;
        pointerCount = event.getPointerCount();
        if (pointerIndex >= 0 && pointerIndex < pointerCount) {
            x = event.getX(pointerIndex);
            y = event.getY(pointerIndex);
            pointerId = event.getPointerId(pointerIndex);
            PointF p = mLastFingerPositions.get(pointerId);
            if (p == null) {
                p = new PointF();
                mLastFingerPositions.put(pointerId, p);
            }
            p.x = x;
            p.y = y;
            if (updateMaster) {
                mMasterPointerId = pointerId;
            }
            return p;
        } else {
            Log.w(TAG, "updateLastPointer Invalid pointerIndex:" + pointerIndex + " overflow pointerCount:" + pointerCount);
        }
        return null;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        //Log.d(TAG, "onTouchEvent:" + event);
        int pointerCount = 0;
        int pointerId = 0;
        int pointerIndex = 0;
        float x = 0;
        float y = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            {
                reset();
                PointF p = updateLastPointer(event, 0, true);
                if (p != null && mListener != null) {
                    mListener.onSlideStart(p);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                if (mMasterPointerId == -1) {
                    mMasterPointerId = event.getPointerId(0);
                }
                PointF oldp = mLastFingerPositions.get(mMasterPointerId);
                if (oldp == null) {
                    updateLastPointer(event, 0, true);
                } else {
                    PointF copyOldP = new PointF(oldp.x, oldp.y);
                    PointF newp = updateLastPointer(event, event.findPointerIndex(mMasterPointerId), false);
                    if (newp == null) {
                        // the masterPointerId may is invalid, now we try to set a correct value
                        updateLastPointer(event, 0, true);
                    } else {
                        if (mListener != null) {
                            mListener.onSlideTo(copyOldP, newp);
                        }
                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            {
                PointF p = updateLastPointer(event, 0, true);
                if (p == null) {
                    if (mLastFingerPositions.size() > 0) {
                        p = mLastFingerPositions.values().iterator().next();
                    }
                    if (p == null) {
                        Log.d(TAG, "fail to find a pointer for slide end, use event's position as default");
                        p = new PointF(event.getX(), event.getY());
                    }
                }
                if (mListener != null) {
                    mListener.onSlideEnd(p);
                }

                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
            {
                // do nothing
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
            {
                pointerIndex = event.getActionIndex();
                pointerId = event.getPointerId(pointerIndex);

                pointerCount = event.getPointerCount();
                if (pointerId == mMasterPointerId) {
                    // try to record the remaining pointer for next MOVE_ACTION from position
                    for (int i = 0; i < pointerCount; i++) {
                        int pi = i;
                        int pid = event.getPointerId(pi);
                        if (pid == pointerId) {
                            continue;
                        }
                        updateLastPointer(event, pi, false);
                    }
                    mLastFingerPositions.remove(mMasterPointerId);
                    mMasterPointerId = -1;
                }
                break;
            }
            default:
                Log.d(TAG, "Unhandled touch event action " + action);
                break;
        }
        return false;
    }
}
