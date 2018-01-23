package coming.example.lkc.bottomnavigationbar.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lkc on 2017/8/1.
 */
public class WeiXin_Content_list implements Parcelable {
    @SerializedName("date")
    public String timedate;
    public String ct;
    @SerializedName("url")
    public String infourl;
    @SerializedName("title")
    public String weixintitle;
    public String contentImg;
    public String userLogo;
    public String userName;

    protected WeiXin_Content_list(Parcel in) {
        timedate = in.readString();
        ct = in.readString();
        infourl = in.readString();
        weixintitle = in.readString();
        contentImg = in.readString();
        userLogo = in.readString();
        userName = in.readString();
    }

    public static final Creator<WeiXin_Content_list> CREATOR = new Creator<WeiXin_Content_list>() {
        @Override
        public WeiXin_Content_list createFromParcel(Parcel in) {
            return new WeiXin_Content_list(in);
        }

        @Override
        public WeiXin_Content_list[] newArray(int size) {
            return new WeiXin_Content_list[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(timedate);
        parcel.writeString(ct);
        parcel.writeString(infourl);
        parcel.writeString(weixintitle);
        parcel.writeString(contentImg);
        parcel.writeString(userLogo);
        parcel.writeString(userName);
    }
}
