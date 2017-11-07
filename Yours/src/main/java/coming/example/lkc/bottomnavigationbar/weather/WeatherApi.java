package coming.example.lkc.bottomnavigationbar.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 李康成 on 2017/11/7.
 */

public class WeatherApi {
    @SerializedName("HeWeather")
    public List<Weather> weather;
}
