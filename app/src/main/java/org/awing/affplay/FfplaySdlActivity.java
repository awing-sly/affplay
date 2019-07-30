package org.awing.affplay;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.libsdl.app.SDLActivity;

public class FfplaySdlActivity extends SDLActivity {
    private static final String TAG = "FfplaySdlActivity";

    private PlayerControllerView mControllerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected String[] getLibraries() {
        return new String[] {
                "SDL2",
                // "SDL2_image",
                // "SDL2_mixer",
                // "SDL2_net",
                // "SDL2_ttf",
                "ffplay-lib"
        };
    }

    @Override
    protected String[] getArguments() {
        String[] args = new String[]{"ffplay", Ffplayer.getInstance(this).getMediaPath()};
        Log.d(TAG, "getArguments=" + args);
        return args;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected View getControllerView() {
        if (mControllerView == null) {
            mControllerView = new PlayerControllerView(this);
        }
        return mControllerView;
    }

    @Override
    protected TouchSlideManager.OnSlideListener getSlideListener() {
        if (mControllerView != null) {
            return mControllerView;
        }
        return super.getSlideListener();
    }
}
