package org.awing.affplay;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.awing.affplay.util.ChooseFileUtils;
import org.awing.affplay.util.PermissionUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_OPEN_FILE = 10;

    private Button mBtnOpen;
    private Button mBtnPlay;
    private EditText mEdtMediaPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOpen = (Button) findViewById(R.id.btn_open);
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mEdtMediaPath = (EditText) findViewById(R.id.edt_path);
        mBtnOpen.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);

        PermissionUtils.verifyStoragePermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult requestCode:" + requestCode);
        if (!PermissionUtils.isStoragePermissionsGranted(requestCode, permissions, grantResults)) {
            finish();
            return;
        }

    }


    private void  updateMediaPath(String path) {
        if (!TextUtils.isEmpty(path) && new File(path).exists()) {
            mEdtMediaPath.setText(path);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnOpen) {
            ChooseFileUtils.startChooseFile(this, REQUEST_OPEN_FILE, "*/*");
        } else if (v == mBtnPlay) {
            String mediaPath = mEdtMediaPath.getText().toString();
            if (!TextUtils.isEmpty(mediaPath) && new File(mediaPath).exists()) {
                Ffplayer.getInstance(this).startPlayActivity(mediaPath);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_OPEN_FILE) {
                Uri uri = data.getData();
                String filePath = ChooseFileUtils.getFilePathByUri(this, uri);
                Log.d(TAG, "Opened file path:" + filePath);
                updateMediaPath(filePath);
            }
        }
    }
}
