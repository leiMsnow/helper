package io.rong.imkit.util;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.R;
import io.rong.imkit.model.Emoji;

public class AndroidEmoji {


    private static Resources sResources;

    public static void init(Context context) {
        sEmojiMap = new HashMap<>();
        sEmojiList = new ArrayList<>();
        sResources = context.getResources();

        int[] codes = sResources.getIntArray(R.array.emoji_code);
        TypedArray array = sResources.obtainTypedArray(R.array.emoji_res);

        if (codes.length != array.length()) {
            throw new RuntimeException("Emoji resource init fail.");
        }

        int i = -1;
        while (++i < codes.length) {
            Emoji emoji = new Emoji(codes[i], array.getResourceId(i, -1));

            sEmojiMap.put(codes[i], emoji);
            sEmojiList.add(emoji);
        }
    }

    public static List<Emoji> getEmojiList(){
        return sEmojiList;
    }

    private static Map<Integer, Emoji> sEmojiMap;
    private static List<Emoji> sEmojiList;

    public static class EmojiImageSpan extends DynamicDrawableSpan {
        Drawable mDrawable;

        private EmojiImageSpan(Resources resources, int codePoint) {
            super(ALIGN_BOTTOM);

            if (sEmojiMap.containsKey(codePoint)) {
                mDrawable = resources.getDrawable(sEmojiMap.get(codePoint).getRes());

                int width = mDrawable.getIntrinsicWidth();
                int height = mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
            }
        }


        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }
    }

    public static int getEmojiCount(String input){
        if (input == null) {
            return 0;
        }

        int count = 0;

        // extract the single chars that will be operated on
        final char[] chars = input.toCharArray();
        // create a SpannableStringBuilder instance where the font ranges will be set for emoji characters
        final SpannableStringBuilder ssb = new SpannableStringBuilder(input);

        int codePoint;
        boolean isSurrogatePair;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isHighSurrogate(chars[i])) {
                continue;
            }
            else if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                }
                else {
                    continue;
                }
            }
            else {
                codePoint = (int) chars[i];
                isSurrogatePair = false;
            }

            if (sEmojiMap.containsKey(codePoint)) {
                count++;
            }
        }
        return count;
    }

    public static CharSequence ensure(String input) {

        if (input == null) {
            return input;
        }

        // extract the single chars that will be operated on
        final char[] chars = input.toCharArray();
        // create a SpannableStringBuilder instance where the font ranges will be set for emoji characters
        final SpannableStringBuilder ssb = new SpannableStringBuilder(input);

        int codePoint;
        boolean isSurrogatePair;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isHighSurrogate(chars[i])) {
                continue;
            }
            else if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                }
                else {
                    continue;
                }
            }
            else {
                codePoint = (int) chars[i];
                isSurrogatePair = false;
            }

            if (sEmojiMap.containsKey(codePoint)) {
                ssb.setSpan(new EmojiImageSpan(sResources, codePoint), isSurrogatePair ? i - 1 : i, i+1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }

        return ssb;
    }

    public static void ensure(Spannable spannable){


        // extract the single chars that will be operated on
        final char[] chars = spannable.toString().toCharArray();
        // create a SpannableStringBuilder instance where the font ranges will be set for emoji characters

        int codePoint;
        boolean isSurrogatePair;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isHighSurrogate(chars[i])) {
                continue;
            }
            else if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                }
                else {
                    continue;
                }
            }
            else {
                codePoint = (int) chars[i];
                isSurrogatePair = false;
            }

            if (sEmojiMap.containsKey(codePoint)) {
                spannable.setSpan(new EmojiImageSpan(sResources, codePoint), isSurrogatePair ? i - 1 : i, i+1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
    }

}
