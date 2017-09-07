package coming.example.lkc.bottomnavigationbar.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lkc on 2017/9/5.
 */
public class JiSuApi_Result {
    public String channel;
    public int num;
    @SerializedName("list")
    public List<JiSuApi_List> Newslist;
}
