package io.rong.imkit.message;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import com.sea_monster.common.ParcelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by zhangleilei on 11/16/15.
 */
@MessageTag(value = "TB:CardMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CardMessage extends MessageContent {

    private String title;
    private String serviceType;
    private String price = "";
    private String url = "";
    private String extra = "";


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardMessage> CREATOR = new Creator() {
        public CardMessage createFromParcel(Parcel source) {
            return new CardMessage(source);
        }

        public CardMessage[] newArray(int size) {
            return new CardMessage[size];
        }
    };

    public CardMessage(String title, String serviceType, String price, String url) {
        if (!TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(serviceType)
                && !TextUtils.isEmpty(price)
                && !TextUtils.isEmpty(url)) {
            this.title = title;
            this.serviceType = serviceType;
            this.price = price;
            this.url = url;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static CardMessage obtain(String title, String content, String price, String url) {
        return new CardMessage(title, content, price, url);
    }

    protected CardMessage(Parcel in) {
        this.title = ParcelUtils.readFromParcel(in);
        this.serviceType = ParcelUtils.readFromParcel(in);
        this.price = ParcelUtils.readFromParcel(in);
        this.url = ParcelUtils.readFromParcel(in);
        this.extra = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public CardMessage() {
    }

    public CardMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject e = new JSONObject(jsonStr);
            this.title = e.optString("title");
            this.serviceType = e.optString("serviceType");
            this.price = e.optString("price");
            this.url = e.optString("url");
            this.extra = e.optString("extra");
            if (e.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(e.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("title", this.getExpression(this.getTitle()));
            jsonObj.put("serviceType", this.getExpression(this.getServiceType()));
            jsonObj.put("price", this.getPrice());
            jsonObj.put("url", this.getUrl());
            jsonObj.put("extra", this.getExtra());
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.title);
        ParcelUtils.writeToParcel(dest, this.serviceType);
        ParcelUtils.writeToParcel(dest, this.price);
        ParcelUtils.writeToParcel(dest, this.url);
        ParcelUtils.writeToParcel(dest, this.extra);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    private String getExpression(String content) {
        Pattern pattern = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, this.toExpressionChar(matcher.group(1)));
        }

        matcher.appendTail(sb);
        Log.d("getExpression--", sb.toString());
        return sb.toString();
    }

    private String toExpressionChar(String expChar) {
        int inthex = Integer.parseInt(expChar, 16);
        return String.valueOf(Character.toChars(inthex));
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
