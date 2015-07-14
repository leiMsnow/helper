package io.rong.imkit.widget.provider;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.MessageInputFragment;
import io.rong.imkit.model.Event;
import io.rong.imkit.widget.InputView;
import io.rong.imlib.RongIMClient;
import io.rong.message.VoiceMessage;

/**
 * Created by DragonJ on 15/3/2.
 */
public class VoiceInputProvider extends InputProvider.MainInputProvider implements View.OnTouchListener, Handler.Callback {

    Button mVoiceBtn;
    PopupWindow mPopWindow;
    LayoutInflater mInflater;
    float lastTouchY;
    float mOffsetLimit;
    private AudioManager mAudioManager;
    private Uri mCurrentRecUri;
    long mVoiceLength;

    private ImageView mIcon;
    private TextView mText, mMessage;
    private Handler mHandler;
    private int mStatus = MSG_NORMAL;

    private static final int MSG_NORMAL = 0;
    private static final int MSG_SEC = 1;
    private static final int MSG_REC = 6;
    private static final int MSG_CANCEL = 2;
    private static final int MSG_SHORT = 7;
    private static final int MSG_SAMPLING = 3;
    private static final int MSG_COMPLETE = 5;
    private static final int MSG_READY = 4;
    private MediaRecorder mMediaRecorder;

    @Override
    public void onAttached(MessageInputFragment fragment, InputView inputView) {
        super.onAttached(fragment, inputView);
        mHandler = new Handler(fragment.getActivity().getMainLooper(), this);
        mAudioManager = (AudioManager) fragment.getActivity().getSystemService(Context.AUDIO_SERVICE);
        mOffsetLimit = 70 * fragment.getActivity().getResources().getDisplayMetrics().density;

    }

    @Override
    public void onSwitch(Context context) {

    }

    @Override
    public void onDetached() {
        super.onDetached();
    }

    public VoiceInputProvider(RongContext context) {
        super(context);
        EventBus.getDefault().register(this);
    }


    @Override
    public Drawable obtainSwitchDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ic_voice);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, InputView inputView) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.rc_wi_vo_provider, parent);
        mVoiceBtn = (Button) view.findViewById(android.R.id.button1);

        mVoiceBtn.setOnTouchListener(this);

        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onActive(v.getContext());
            lastTouchY = event.getY();
            mHandler.obtainMessage(MSG_READY, v.getRootView()).sendToTarget();
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {

            if (event.getEventTime() - event.getDownTime() < 1000)
                mStatus = MSG_SHORT;

            mHandler.obtainMessage(MSG_COMPLETE).sendToTarget();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (lastTouchY - event.getY() > mOffsetLimit) {
                mHandler.obtainMessage(MSG_CANCEL).sendToTarget();
            } else {
                mHandler.obtainMessage(MSG_REC).sendToTarget();
            }
        }


        return true;
    }

    @Override
    public void onActive(Context context) {
    }

    @Override
    public void onInactive(Context context) {

    }

    boolean isCancel = false;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_READY:

                if (mPopWindow == null) {
                    View view = mInflater.inflate(R.layout.rc_wi_vo_popup, null);

                    mIcon = (ImageView) view.findViewById(android.R.id.icon);
                    mText = (TextView) view.findViewById(android.R.id.text1);
                    mMessage = (TextView) view.findViewById(android.R.id.message);
                    mPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    View parent = (View) msg.obj;
                    mPopWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                    mPopWindow.setFocusable(true);
                    mPopWindow.setOutsideTouchable(false);
                    mPopWindow.setTouchable(false);
                }

                startRec();


                mStatus = MSG_READY;
                mVoiceLength = SystemClock.elapsedRealtime();
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SEC, 10, 0),
                        50 * 1000);

                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SAMPLING),
                        150);

                mMessage.setText(R.string.rc_voice_rec);
                mIcon.setImageResource(R.drawable.rc_ic_volume_1);
                mMessage.setBackgroundColor(Color.TRANSPARENT);
                mText.setVisibility(View.GONE);


                break;
            case MSG_REC:

                if (mStatus == MSG_CANCEL) {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SAMPLING), 150);
                }

                mStatus = MSG_REC;
                mMessage.setText(R.string.rc_voice_rec);
                mIcon.setImageResource(R.drawable.rc_ic_volume_1);
                mMessage.setBackgroundColor(Color.TRANSPARENT);
                mText.setVisibility(View.GONE);

                break;

            case MSG_CANCEL:

                mMessage.setText(R.string.rc_voice_cancel);
                mIcon.setImageResource(R.drawable.rc_ic_volume_cancel);
                mMessage.setBackgroundColor(Color.RED);

                mStatus = MSG_CANCEL;

                break;

            case MSG_SEC:

                if (mStatus != MSG_CANCEL) {
                    mText.setVisibility(View.VISIBLE);
                    mText.setText(msg.arg1 + "s");
                    if (msg.arg1 > 0)
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SEC, --msg.arg1, 0), 1000);
                    else {
                        mHandler.sendEmptyMessage(MSG_COMPLETE);
                    }
                }
                break;

            case MSG_SAMPLING:

                if (mStatus == MSG_CANCEL || mStatus == MSG_SHORT)
                    break;

                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SAMPLING),
                        150);

                int db = getCurrentVoiceDb();
                switch (db / 5) {
                    case 0:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_1);
                        break;
                    case 1:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_2);
                        break;
                    case 2:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_3);
                        break;
                    case 3:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_4);
                        break;
                    case 4:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_5);
                        break;
                    case 5:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_6);
                        break;
                    case 6:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_7);
                        break;
                    default:
                        mIcon.setImageResource(R.drawable.rc_ic_volume_8);
                        break;
                }
                break;

            case MSG_COMPLETE:
                RLog.e(this, "handleMessage", "----MSG_COMPLETE-----");
                mHandler.removeMessages(MSG_READY);
                mHandler.removeMessages(MSG_SEC);
                mHandler.removeMessages(MSG_CANCEL);

                if (mStatus == MSG_CANCEL) {
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        {
                            mPopWindow.dismiss();
                            mPopWindow = null;
                            stopRec(false);
                        }
                    }
                } else if (mStatus == MSG_SHORT) {

                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mIcon.setImageResource(R.drawable.rc_ic_volume_wraning);
                        mMessage.setText(R.string.rc_voice_short);

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mPopWindow != null && mPopWindow.isShowing()) {
                                    {
                                        mPopWindow.dismiss();
                                        mPopWindow = null;
                                        stopRec(false);
                                    }
                                }
                            }
                        }, 800);
                    }


                } else {
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                        mPopWindow = null;
                    }
                    stopRec(true);
                }


                break;


        }
        return false;
    }

    public class VoiceException extends RuntimeException {
        public VoiceException(Throwable e) {
            super(e);
        }
    }

    public int getCurrentVoiceDb() {
        if (mMediaRecorder == null)
            return 0;
        return mMediaRecorder.getMaxAmplitude() / 600;
    }

    public void startRec() throws VoiceException {

        RongContext.getInstance().getEventBus().post(Event.VoiceInputOperationEvent.obtain(Event.VoiceInputOperationEvent.STATUS_INPUTING));

        mAudioManager.setMode(AudioManager.MODE_NORMAL);

        try {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mCurrentRecUri = Uri.fromFile(new File(mContext.getCacheDir(), "temp.voice"));
            mMediaRecorder.setOutputFile(mCurrentRecUri.getPath());

            mMediaRecorder.prepare();

            mMediaRecorder.start();
        } catch (RuntimeException ex) {
            if(mMediaRecorder != null) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
            }
            mMediaRecorder = null;
            ex.printStackTrace();
//            throw new VoiceException(ex);
        } catch (IOException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            e.printStackTrace();
//            throw new VoiceException(e);
        }

        mStatus = MSG_READY;
    }


    public Uri stopRec(boolean save) throws VoiceException {

        if (mMediaRecorder == null)
            return null;

        RongContext.getInstance().getEventBus().post(Event.VoiceInputOperationEvent.obtain(Event.VoiceInputOperationEvent.STATUS_INPUT_COMPLETE));

        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (RuntimeException e) {
            throw new VoiceException(e);
        }

        Uri temp = mCurrentRecUri;
        mCurrentRecUri = null;

        if (!save) {
            File file = new File(temp.getPath());
            if (file.exists())
                file.delete();
        } else {
            int length = (int) ((SystemClock.elapsedRealtime() - mVoiceLength) / 1000);
            if (length == 0) {
                return null;
            }

            File file = new File(temp.getPath());
            if(!file.exists())
                return null;
            
            publish(VoiceMessage.obtain(temp, (int) (SystemClock.elapsedRealtime() - mVoiceLength) / 1000), new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                @Override
                public void onSuccess(io.rong.imlib.model.Message message) {
                    io.rong.imlib.model.Message.ReceivedStatus status = message.getReceivedStatus();
                    status.setListened();
                    RongIM.getInstance().getRongIMClient().setMessageReceivedStatus(message.getMessageId(), status);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });

        }

        mStatus = MSG_NORMAL;
        return temp;
    }

    public void onEvent(InputView.Event event) {
        if (event.equals(InputView.Event.INACTION)) {
            RLog.d(this, "InputView", event.toString());
        }
    }
}
