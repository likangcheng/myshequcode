package coming.example.lkc.bottomnavigationbar.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lkc on 2017/8/1.
 */
public class WeiXin_Content_list implements Serializable {
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
}
