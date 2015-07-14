package io.rong.imkit.widget.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import io.rong.database.ConversationDatabase;
import io.rong.database.Draft;
import io.rong.database.DraftDao;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.MessageInputFragment;
import io.rong.imkit.model.Emoji;
import io.rong.imkit.util.AndroidEmoji;
import io.rong.imkit.widget.CircleFlowIndicator;
import io.rong.imkit.widget.InputView;
import io.rong.imkit.widget.ViewFlow;
import io.rong.imkit.widget.adapter.EmojiPagerAdapter;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by DragonJ on 15/2/11.
 */
public class TextInputProvider extends InputProvider.MainInputProvider implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener, EmojiPagerAdapter.OnEmojiItemClickListener {

    EditText mEdit;
    ImageView mSmile;
    Button mButton;
    InputView mInputView;
    LayoutInflater mInflater;
    FragmentManager mFragmentManager;
    ViewFlow mFlow;
    Context mContext;
    Handler mHandler;
    TextWatcher mExtraTextWatcher;


    public TextInputProvider(RongContext context) {
        super(context);
        RLog.d(this, "TextInputProvider", "");

    }

    @Override
    public void onAttached(MessageInputFragment fragment, InputView view) {
        super.onAttached(fragment, view);
        mContext = fragment.getActivity();
        mFragmentManager = fragment.getActivity().getSupportFragmentManager();
        mHandler = new Handler();

        RLog.d(this, "onAttached", "");
    }

    @Override
    public void onDetached() {
        mFlow = null;
        mButton = null;

        RLog.d(this, "Detached", "");

        if (mEdit != null && !TextUtils.isEmpty(mEdit.getText())) {
            RongContext.getInstance().executorBackground(new SaveDraftRunnable(getCurrentConversation(), mEdit.getText().toString()));
        } else {
            RongContext.getInstance().executorBackground(new CleanDraftRunnable(getCurrentConversation()));
        }

        super.onDetached();
    }

    @Override
    public Drawable obtainSwitchDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ic_keyboard);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, InputView inputView) {
        RLog.d(this, "onCreateView", "");
        mInflater = inflater;

        View view = inflater.inflate(R.layout.rc_wi_txt_provider, parent);
        mEdit = (EditText) view.findViewById(android.R.id.edit);
        mSmile = (ImageView) view.findViewById(android.R.id.icon);

        if (inputView.getToggleLayout().getVisibility() == View.VISIBLE) {
            mButton = (Button) inflater.inflate(R.layout.rc_wi_text_btn, inputView.getToggleLayout(), false);
            inputView.getToggleLayout().addView(mButton);
        }

        if (mButton == null)
            mButton = (Button) view.findViewById(android.R.id.button1);

        mEdit.addTextChangedListener(this);
        mEdit.setOnFocusChangeListener(this);
        mSmile.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mInputView = inputView;
        mButton.setOnClickListener(this);

        if (mExtraTextWatcher != null)
            mEdit.addTextChangedListener(mExtraTextWatcher);

        RongContext.getInstance().executorBackground(new DraftRenderRunnable(getCurrentConversation()));

        return view;
    }

    @Override
    public void setCurrentConversation(Conversation conversation) {
        super.setCurrentConversation(conversation);
        RongContext.getInstance().executorBackground(new DraftRenderRunnable(conversation));
    }

    class DraftRenderRunnable implements Runnable {
        Conversation conversation;

        DraftRenderRunnable(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public void run() {

            int type = conversation.getConversationType().getValue();
            String targetId = conversation.getTargetId();
            if (targetId == null)
                return;

            final Draft draft = ConversationDatabase.getDraftDao().queryBuilder().where(DraftDao.Properties.Type.eq(type), DraftDao.Properties.Id.eq(targetId)).unique();

            if (draft != null && !TextUtils.isEmpty(draft.getContent()) && mHandler != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mEdit.setText(draft.getContent());
                        mEdit.setSelection(draft.getContent().length());
                    }
                }, 70);
            }
        }
    }

    class SaveDraftRunnable implements Runnable {
        String content;
        Conversation conversation;

        SaveDraftRunnable(Conversation conversation, String content) {
            this.conversation = conversation;
            this.content = content;
        }

        @Override
        public void run() {
            int type = conversation.getConversationType().getValue();
            String targetId = conversation.getTargetId();

            Draft draft = new Draft(targetId, type, content, null);
            ConversationDatabase.getDraftDao().insertOrReplace(draft);
            getContext().getEventBus().post(draft);

        }
    }

    class CleanDraftRunnable implements Runnable {
        Conversation conversation;

        CleanDraftRunnable(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public void run() {

            int type = conversation.getConversationType().getValue();
            String targetId = conversation.getTargetId();

            if (ConversationDatabase.getDraftDao() != null)
                ConversationDatabase.getDraftDao().queryBuilder().where(DraftDao.Properties.Type.eq(type), DraftDao.Properties.Id.eq(targetId)).buildDelete().executeDeleteWithoutDetachingEntities();

            Draft draft = new Draft(targetId, type, null, null);

            getContext().getEventBus().post(draft);

        }
    }

    @Override
    public void onClick(View v) {
        if (mSmile.equals(v)) {

            if (mFlow == null) {
                View view = mInflater.inflate(R.layout.rc_wi_ext_pager, mInputView.getExtendLayout());
                mFlow = (ViewFlow) view.findViewById(R.id.rc_flow);
                CircleFlowIndicator indicator = (CircleFlowIndicator) view.findViewById(R.id.rc_indicator);
                mFlow.setFlowIndicator(indicator);

                final EmojiPagerAdapter adapter = new EmojiPagerAdapter(mContext, mInputView.getExtendLayout(), AndroidEmoji.getEmojiList(), mFragmentManager);
                adapter.setEmojiItemClickListener(this);

                mFlow.setAdapter(adapter);
                mInputView.onProviderInactive(getContext());
                mInputView.setExtendLayoutVisibility(View.VISIBLE);
            } else if (mInputView.getExtendLayout().getVisibility() == View.GONE) {
                mInputView.onProviderInactive(getContext());
                mInputView.setExtendLayoutVisibility(View.VISIBLE);
            } else {
                mInputView.onProviderInactive(getContext());
            }
        } else if (v.equals(mButton)) {
            if (TextUtils.isEmpty(mEdit.getText().toString().trim())) {
                mEdit.getText().clear();
                return;
            }

            publish(TextMessage.obtain(mEdit.getText().toString()));
            mEdit.getText().clear();
        } else if (mEdit.equals(v)) {
            mInputView.onProviderActive(getContext());
        }
    }


    @Override
    public void onActive(Context context) {
        if(mEdit == null)
            return;

        mEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEdit, 0);
    }

    @Override
    public void onInactive(Context context) {
        if(mEdit == null)
            return;

        mEdit.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
    }

    @Override
    public void onSwitch(Context context) {
        mButton.setVisibility(View.GONE);
        onInactive(context);
        if (mEdit != null && !TextUtils.isEmpty(mEdit.getText())) {
            RongContext.getInstance().executorBackground(new SaveDraftRunnable(getCurrentConversation(), mEdit.getText().toString()));
        } else {
            RongContext.getInstance().executorBackground(new CleanDraftRunnable(getCurrentConversation()));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getTag() != null && view.getTag() instanceof Emoji) {
            Emoji emoji = (Emoji) view.getTag();

            for (char item : Character.toChars(emoji.getCode())) {
                mEdit.getText().insert(mEdit.getSelectionStart(), Character.toString(item));
            }

        } else if (view.getTag().equals(-1)) {
            int keyCode = KeyEvent.KEYCODE_DEL;  //这里是退格键
            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);

            KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
            mEdit.onKeyDown(keyCode, keyEventDown);
            mEdit.onKeyUp(keyCode, keyEventUp);
        } else if (view.getTag().equals(0)) {

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mButton.setVisibility(View.GONE);
        } else {
            mButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

        UnderlineSpan[] spans = s.getSpans(0, s.length(), UnderlineSpan.class);

        if (spans != null && spans.length > 0)
            return;

        int start = mEdit.getSelectionStart();
        int end = mEdit.getSelectionEnd();

        mEdit.removeTextChangedListener(this);
        mEdit.setText(AndroidEmoji.ensure(s.toString()));
        mEdit.addTextChangedListener(this);

        mEdit.setSelection(start, end);
        RLog.d(this, "afterTextChanged", s.toString());
    }

    /**
     * 设置输入框
     *
     * @param content
     */
    public void setEditTextContent(CharSequence content) {

        if (mEdit != null && !TextUtils.isEmpty(content)) {
            mEdit.setText(content);
            mEdit.setSelection(content.length());
        }
    }

    public void setEditTextChangedListener(TextWatcher listener) {

        if (mExtraTextWatcher != null)
            mEdit.removeTextChangedListener(mExtraTextWatcher);

        mExtraTextWatcher = listener;

        if (mEdit != null)
            mEdit.addTextChangedListener(mExtraTextWatcher);
    }
}