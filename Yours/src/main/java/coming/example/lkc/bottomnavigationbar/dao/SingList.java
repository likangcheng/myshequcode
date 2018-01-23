package coming.example.lkc.bottomnavigationbar.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by lkc on 2017/8/7.
 */
public class SingList implements Parcelable {
    public String m4a;
    public String songname;
    public String albumpic_big;
    public String albumpic_small;
    public String downUrl;
    @SerializedName("url")
    public String musicurl;
    public String singername;

    public SingList() {
    }

    protected SingList(Parcel in) {
        m4a = in.readString();
        songname = in.readString();
        albumpic_big = in.readString();
        albumpic_small = in.readString();
        downUrl = in.readString();
        musicurl = in.readString();
        singername = in.readString();
    }

    public static final Creator<SingList> CREATOR = new Creator<SingList>() {
        @Override
        public SingList createFromParcel(Parcel in) {
            return new SingList(in);
        }

        @Override
        public SingList[] newArray(int size) {
            return new SingList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(m4a);
        parcel.writeString(songname);
        parcel.writeString(albumpic_big);
        parcel.writeString(albumpic_small);
        parcel.writeString(downUrl);
        parcel.writeString(musicurl);
        parcel.writeString(singername);
    }
}
