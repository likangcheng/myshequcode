package coming.example.lkc.bottomnavigationbar.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lkc on 2017/7/24.
 */
public class Weather {
    public String status;
    public Basic basic;
    public Aqi aqi;
    public Suggestion suggestion;
    public Weather weather;
    public Now now;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
