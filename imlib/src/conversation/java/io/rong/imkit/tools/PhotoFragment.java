package io.rong.imkit.tools;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.BaseFragment;
import io.rong.message.utils.BitmapUtil;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.StoreStatusCallback;
import com.sea_monster.resource.ResCallback;
import com.sea_monster.resource.Resource;
import com.sea_monster.resource.ResourceHandler;
import uk.co.senab.photoview.PhotoView;

/**
* Created by DragonJ on 15/4/13.
*/
public class PhotoFragment extends BaseFragment {
    PhotoView mPhotoView;
    Uri mUri;
    Uri mThumbnail;
    ImageProcess mProcess;
    ProgressBar mProgressBar;
    TextView mProgressText;
    PhotoDownloadListener mListener;
    Handler mHandler;

    final static int GET_PHOTO = 0x1;
    final static int REQ_PHOTO = 0x2;
    final static int SHOW_PHOTO = 0x3;
    final static int DOWNLOAD_PROGRESS = 0x4;
    final static int REQ_FAILURE = 0x5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fr_photo, container, false);
        mPhotoView = (PhotoView) view.findViewById(R.id.rc_icon);
        mProgressBar = (ProgressBar) view.findViewById(R.id.rc_progress);
        mProgressText = (TextView) view.findViewById(R.id.rc_txt);

        HandlerThread thread = new HandlerThread("DOWNLOAD_PHOTO");
        thread.start();
        mHandler = new Handler(thread.getLooper());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case GET_PHOTO:
                Uri uri = (Uri) msg.obj;

                if (mProcess != null)
                    mProcess.cancel(true);

                mProcess = new ImageProcess();
                mProcess.execute(uri);
                mProgressBar.setVisibility(View.GONE);
                mProgressText.setVisibility(View.GONE);
                if(mListener != null)
                    mListener.onDownloaded(uri);
                break;
            case REQ_PHOTO:
                mHandler.post(new Runnable() {
                    final Uri uri = mUri;
                    @Override
                    public void run() {
                        try {
                            ResourceHandler.getInstance().requestResource(new Resource(uri), new ResCallback() {
                                @Override
                                public void onComplete(AbstractHttpRequest<File> abstractHttpRequest, File file) {
                                    RLog.i(PhotoFragment.this, "onComplete", file.getPath());
                                    getHandler().obtainMessage(GET_PHOTO, Uri.fromFile(file)).sendToTarget();
                                }

                                @Override
                                public void onFailure(AbstractHttpRequest<File> abstractHttpRequest, BaseException e) {
                                    RLog.e(PhotoFragment.this, "onFailure", e.toString(), e);
                                    File file = ResourceHandler.getInstance().getFile(new Resource(uri));
                                    if(file != null && file.exists()) {
                                        file.delete();
                                    }
                                    getHandler().obtainMessage(REQ_FAILURE).sendToTarget();
                                }
                            }, mRcvDataCallback);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressText.setVisibility(View.VISIBLE);
                mProgressText.setText(0+"%");
                break;
            case SHOW_PHOTO:
                mPhotoView.setImageBitmap((Bitmap) msg.obj);
                break;
            case DOWNLOAD_PROGRESS:
                int total = msg.arg1;
                int received = msg.arg2;
                if(received < total){
                    mProgressText.setText((received * 100)/total+"%");
                }
                break;
            case REQ_FAILURE:
                mProgressBar.setVisibility(View.GONE);
                mProgressText.setText("下载失败!");
                if(mListener != null)
                    mListener.onDownloadError();
                break;
        }

        return true;
    }

    private StoreStatusCallback mRcvDataCallback = new StoreStatusCallback() {
        @Override
        public void statusCallback(StoreStatus storeStatus) {
            getHandler().obtainMessage(DOWNLOAD_PROGRESS, (int)storeStatus.getTotalSize(), (int)storeStatus.getReceivedSize())
                        .sendToTarget();
        }
    };

    public void initPhoto(final Uri uri, final Uri thumbnail, PhotoDownloadListener listener) {
        mUri = uri;
        mThumbnail = thumbnail;
        mProcess = new ImageProcess();
        mListener= listener;

        if (mUri.getScheme().equals("http")) {

            RongContext.getInstance().executorBackground(new Runnable() {
                @Override
                public void run() {
                    if (ResourceHandler.getInstance().containsInDiskCache(new Resource(mUri))) {
                        mUri = Uri.fromFile(ResourceHandler.getInstance().getFile(new Resource(mUri)));
                        getHandler().obtainMessage(GET_PHOTO, mUri).sendToTarget();
                    } else {
                        getHandler().obtainMessage(REQ_PHOTO, mUri).sendToTarget();
                    }
                }
            });

            if(mThumbnail != null) {
                mProcess.execute(mThumbnail);
            }
        } else {
            mProcess.execute(mUri);
        }

    }


    class ImageProcess extends AsyncTask<Uri, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {

            Bitmap bitmap = null;

            Uri param = params[0];
            Uri uri = null;


            if (param.getScheme().equals("file")) {
                uri = param;
            } else if (param.getScheme().equals("content")) {
                Cursor cursor = getActivity().getContentResolver().query(param, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);

                if (cursor == null || cursor.getCount() == 0) {
                    cursor.close();
                    return null;
                }

                cursor.moveToFirst();
                uri = Uri.parse("file://" + cursor.getString(0));
                cursor.close();
            }

            try {
                bitmap = BitmapUtil.getResizedBitmap(getActivity(), uri, 960, 960);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            if (result != null)
                mPhotoView.setImageBitmap(result);
        }
    }

    @Override
    public void onRestoreUI(){}

    public interface PhotoDownloadListener {
        public void onDownloaded(Uri file);
        public void onDownloadError();
    }
}
