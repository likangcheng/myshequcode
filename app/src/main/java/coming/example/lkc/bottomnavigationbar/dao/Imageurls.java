package coming.example.lkc.bottomnavigationbar.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lkc on 2017/7/31.
 */
public class Imageurls implements Serializable {
    @SerializedName("url")
    public String imgurl;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
