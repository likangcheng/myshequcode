package coming.example.lkc.bottomnavigationbar.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lkc on 2017/7/24.
 */
public class Aqi {
    @SerializedName("city")
    public AqiCity aqiCity;

    public class AqiCity {
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
