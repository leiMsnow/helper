package io.rong.imkit.widget.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.tools.SelectPictureActivity;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

/**
 * Created by DragonJ on 15/3/4.
 */
public class ImageInputProvider extends InputProvider.ExtendProvider {

    HandlerThread mWorkThread;
    Handler mUploadHandler;

    public ImageInputProvider(RongContext context) {
        super(context);
    }

    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ic_picture);
    }

    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return context.getString(R.string.rc_plugins_image);
    }

    @Override
    public void onPluginClick(View view) {
        Intent intent = new Intent();
        intent.setClass(view.getContext(), SelectPictureActivity.class);
        startActivityForResult(intent, 23);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
            return;

        mWorkThread = new HandlerThread("ImageInputProvider");
        mWorkThread.start();
        mUploadHandler = new Handler(mWorkThread.getLooper());

        if (data.getData() != null && "content".equals(data.getData().getScheme())) {

            getContext().executorBackground(new AttachRunnable(data.getData()));

        } else if (data.hasExtra(Intent.EXTRA_RETURN_RESULT)) {

            ArrayList<Uri> uris = data.getParcelableArrayListExtra(Intent.EXTRA_RETURN_RESULT);

            for (Uri item : uris) {
                getContext().executorBackground(new AttachRunnable(item));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class AttachRunnable implements Runnable {

        Uri mUri;

        public AttachRunnable(Uri uri) {
            mUri = uri;
        }

        @Override
        public void run() {
            Cursor cursor = getContext().getContentResolver().query(mUri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);

            if (cursor == null || cursor.getCount() == 0) {
                RLog.d(this, "AttachRunnable", "cursor is not available");
                cursor.close();
                return;
            }

            cursor.moveToFirst();
            Uri uri = Uri.parse("file://" + cursor.getString(0));
            cursor.close();

            RLog.d(this, "AttachRunnable", "insert image and save to db, uri = " + uri);
            final ImageMessage content = ImageMessage.obtain(uri, uri);
            RongIM.getInstance().getRongIMClient().insertMessage(mCurrentConversation.getConversationType(), mCurrentConversation.getTargetId(), null, content, new RongIMClient.ResultCallback<Message>() {
                @Override
                public void onSuccess(Message message) {
                    RLog.d(this, "AttachRunnable", "onSuccess insert image");
                    message.setSentStatus(Message.SentStatus.SENDING);
                    RongIM.getInstance().getRongIMClient().setMessageSentStatus(message.getMessageId(), Message.SentStatus.SENDING, null);
                    mUploadHandler.post(new UploadRunnable(message));
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    RLog.d(this, "AttachRunnable", "onError insert image, error = " + e);
                }
            });
        }
    }

    class UploadRunnable implements Runnable {
        Message msg;
        CountDownLatch mLock;

        public UploadRunnable(Message msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            mLock = new CountDownLatch(1);
            RLog.d(this, "UploadRunnable", "sendImageMessage");
            RongIM.getInstance().getRongIMClient().sendImageMessage(msg, null, new RongIMClient.SendImageMessageCallback() {
                @Override
                public void onAttached(Message message) {
                    mLock.countDown();
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode code) {
                    mLock.countDown();
                }

                @Override
                public void onSuccess(Message message) {
                    mLock.countDown();
                }

                @Override
                public void onProgress(Message message, int progress) {

                }
            });
            try {
                mLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}