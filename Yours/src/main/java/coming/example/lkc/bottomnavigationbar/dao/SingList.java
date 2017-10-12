package coming.example.lkc.bottomnavigationbar.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lkc on 2017/8/7.
 */
public class SingList implements Serializable {
    public String songname;
    public String albumpic_big;
    public String albumpic_small;
    public String downUrl;
    @SerializedName("url")
    public String musicurl;
    public String singername;
}
