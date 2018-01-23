package coming.example.lkc.bottomnavigationbar.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李康成 on 2018/1/17.
 */

public class TestPre implements Parcelable {

    private int mydata;

    protected TestPre(Parcel in) {
        mydata=in.readInt();
    }

    public static final Creator<TestPre> CREATOR = new Creator<TestPre>() {
        @Override
        public TestPre createFromParcel(Parcel in) {
            return new TestPre(in);
        }

        @Override
        public TestPre[] newArray(int size) {
            return new TestPre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mydata);
    }
}
