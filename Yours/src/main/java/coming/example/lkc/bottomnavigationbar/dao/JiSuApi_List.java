package coming.example.lkc.bottomnavigationbar.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lkc on 2017/9/5.
 */
public class JiSuApi_List implements Serializable{
    @SerializedName("title")
    public String NewsTitle;
    @SerializedName("time")
    public String NewsTime;
    @SerializedName("src")
    public String NewsSrc;
    public String pic;
    @SerializedName("url")
    public String NewsUrl;

}
