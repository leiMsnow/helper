package io.rong.imkit.widget.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Toast;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.LocationMessage;

/**
 * Created by zhjchen on 3/17/15.
 */

public class LocationInputProvider extends InputProvider.ExtendProvider {

    HandlerThread mWorkThread;
    Handler mDownloadHandler;
    Message mCurrentMessage;
    int mCurrentProgress;

    public LocationInputProvider(RongContext context) {
        super(context);
    }

    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ic_location);
    }


    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return context.getString(R.string.rc_plugins_location);
    }


    @Override
    public void onPluginClick(View view) {

        mWorkThread = new HandlerThread("LocationInputProvider");
        mWorkThread.start();
        mDownloadHandler = new Handler(mWorkThread.getLooper());

        if (RongContext.getInstance() != null && RongContext.getInstance().getLocationProvider() != null) {

            RongContext.getInstance().getLocationProvider().onStartLocation(getContext(), new RongIM.LocationProvider.LocationCallback() {

                @Override
                public void onSuccess(final LocationMessage locationMessage) {

                    RongIM.getInstance().getRongIMClient().insertMessage(mCurrentConversation.getConversationType(), mCurrentConversation.getTargetId(), RongIM.getInstance().getRongIMClient().getCurrentUserId(), locationMessage, new RongIMClient.ResultCallback<Message>() {
                        @Override
                        public void onSuccess(Message message) {
                            if (locationMessage.getImgUri() != null && locationMessage.getImgUri().getScheme().equals("http")) {
                                message.setContent(locationMessage);
                                mDownloadHandler.post(new DownloadRunnable(message, locationMessage.getImgUri()));
                            } else {
                                Toast.makeText(getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {

                        }
                    });


                }

                @Override
                public void onFailure(String msg) {

                }
            });

        }
    }

    @Override
    public void onDetached() {
        mWorkThread.quit();
        super.onDetached();
    }

    public Message getCurrentMessage() {
        return mCurrentMessage;
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    class DownloadRunnable implements Runnable {
        private Message message;
        private Uri uri;

        public DownloadRunnable(Message message, Uri uri) {
            this.message = message;
            this.uri = uri;

        }

        @Override
        public void run() {

            mCurrentMessage = message;

            RongIM.getInstance().getRongIMClient().downloadMedia(uri.toString(), new RongIMClient.DownloadMediaCallback() {
                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    RLog.w(this, "onFailure", e.toString());
                    getContext().getEventBus().post(e);
                }

                @Override
                public void onProgress(int progress) {
                    RLog.i(this, "onProgress", "progress:" + progress);
                    mCurrentProgress = progress;
                    getContext().getEventBus().post(message);
                }

                @Override
                public void onSuccess(String s) {
                    RLog.i(this, "onSuccess", "url:" + s);

                    LocationMessage locationMessage = (LocationMessage) message.getContent();
                    locationMessage.setImgUri(Uri.parse(s));
                    message.setContent(locationMessage);
                    mCurrentProgress=100;

                    RongIM.getInstance().getRongIMClient().sendMessage(message, null, null);

                }

            });

        }
    }
}
