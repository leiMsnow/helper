package io.rong.imkit.widget.provider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.File;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.tools.PictureFragment;

/**
 * Created by AMing on 2015/4/18.
 */
public class TakingPicturesActivity extends FragmentActivity implements View.OnClickListener {

    private static final int START_CAMERA = 0x1;

    private final static int REQUEST_CAMERA = 0x2;

    private static final String ACTION_CAMERA = "android.media.action.IMAGE_CAPTURE";

    private Button mButtonCancel;

    private Button mButtonSend;

    private Uri mSavedPicUri;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_CAMERA:
                    startCamera();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rc_ac_camera);
        mButtonCancel = (Button) findViewById(R.id.rc_back);
        mButtonSend = (Button) findViewById(R.id.rc_send);

        if(savedInstanceState == null) {
            Message msg = Message.obtain();
            msg.what = START_CAMERA;
            mHandler.sendMessage(msg);
        }

        mButtonCancel.setOnClickListener(this);
        mButtonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final File file = new File(mSavedPicUri.getPath());

        if (!file.exists()) {
            finish();
        }

        if (v.getId() == R.id.rc_send) {
            if (mSavedPicUri != null) {
                Intent data = new Intent();
                data.setData(mSavedPicUri);
                setResult(RESULT_OK, data);
            }
            finish();
        } else if (v.getId() == R.id.rc_back) {
            finish();
        }
    }

    private void startCamera() {
        Intent intent = new Intent(ACTION_CAMERA);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String name = System.currentTimeMillis() + ".jpg";

        File file = new File(path, name);
        mSavedPicUri = Uri.fromFile(file);
        RLog.d(this, "startCamera", "output pic uri =" + mSavedPicUri);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mSavedPicUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    finish();
                    Log.e("TakingPicturesActivity","RESULT_CANCELED");
                }
                if (mSavedPicUri != null && resultCode == Activity.RESULT_OK) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showImage(mSavedPicUri);
                            Log.e("TakingPicturesActivity","RESULT_OK");
                        }
                    }, 10);
                }
                break;
            default:
                return;
        }
    }


    private final void showImage(Uri uri) {
        if (uri != null) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            PictureFragment mPhotoFragment = new PictureFragment(this, uri);
            ft.replace(R.id.rc_frame, mPhotoFragment);
            ft.commitAllowingStateLoss();
        }else{
            finish();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mSavedPicUri =   Uri.parse(savedInstanceState.getString("photo_uri"));
        super.onRestoreInstanceState(savedInstanceState);
        //还原
        Log.e("TakingPicturesActivity","onRestoreInstanceState");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("photo_uri",mSavedPicUri.toString());
        super.onSaveInstanceState(outState);
        //保存
        Log.e("TakingPicturesActivity","onSaveInstanceState");
    }
}
