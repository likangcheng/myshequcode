package coming.example.lkc.bottomnavigationbar.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lkc on 2017/7/24.
 */
public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
