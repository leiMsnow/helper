package io.rong.imkit.widget.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import io.rong.imkit.R;
import io.rong.imkit.model.Emoji;

/**
 * Created by DragonJ on 15/2/28.
 */

public class EmojiPagerAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;

    List<Emoji> mEmojis;
    int columnWidth;
    int columnCount;
    int pageEmojiCount;
    int pageCount;
    OnEmojiItemClickListener mEmojiItemClickListener;

    public EmojiPagerAdapter(Context context, ViewGroup container, List<Emoji> emojis, FragmentManager manager) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        columnWidth = (int) (40 * context.getResources().getDisplayMetrics().density);

        int padding = (int) (20 * context.getResources().getDisplayMetrics().density);

        columnCount = container == null || container.getWidth() == 0 ?
                context.getResources().getDisplayMetrics().widthPixels / columnWidth :
                (container.getWidth() - container.getPaddingRight() - container.getPaddingLeft() - padding) / columnWidth;

        pageEmojiCount = columnCount * ((columnCount + 2) / 3) - 1;
        pageCount = (emojis.size() + pageEmojiCount - 1) / pageEmojiCount;
        mEmojis = emojis;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rc_wi_emoji_grid, null);
        }

        GridView gridView = (GridView) convertView;
        Log.d("EmojiPagerAdapter", "getView position:" + position);
        gridView.setAdapter(new EmojiAdapter(pageEmojiCount, position * pageEmojiCount, mEmojis));
        if (mEmojiItemClickListener != null) {
            gridView.setOnItemClickListener(mEmojiItemClickListener);
        }

        return gridView;
    }

    public void setEmojiItemClickListener(OnEmojiItemClickListener emojiItemClickListener) {
        this.mEmojiItemClickListener = emojiItemClickListener;
    }

    public interface OnEmojiItemClickListener extends AdapterView.OnItemClickListener {
    }


    class EmojiAdapter extends BaseAdapter {

        int mCount;
        List<Emoji> mEmojis;
        int mOffset;

        public EmojiAdapter(int count, int offset, List<Emoji> emojis) {
            mCount = count + 1;
            mOffset = offset;
            Log.d("EmojiAdapter", " offset:" + mOffset + " mCount:" + mCount);

            mEmojis = emojis.subList(offset, emojis.size() > (offset + count) ? (offset + count) : emojis.size());

            while (mEmojis.size() < count) {
                mEmojis.add(new Emoji(0, 0));
            }
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Emoji getItem(int position) {
            return mEmojis.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.rc_wi_emoji_txt, null);
            }

            ImageView emoji = (ImageView) convertView;
            if (position == mCount - 1) {
                emoji.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rc_ic_delete));
                emoji.setTag(-1);
            } else {
                if (mEmojis.get(position).getCode() == 0) {
                    emoji.setTag(0);
                    emoji.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rc_ic_emoji_block));
                } else {
                    emoji.setTag(mEmojis.get(position));
                    emoji.setImageResource(mEmojis.get(position).getRes());
                }
            }
            return convertView;
        }
    }
}


