package coming.example.lkc.bottomnavigationbar.unitl;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;
import coming.example.lkc.bottomnavigationbar.weather.Weather;
import coming.example.lkc.bottomnavigationbar.weather.WeatherApi;

/**
 * Created by lkc on 2017/7/31.
 */
public class Utility {
    public static JiSuApi_Body handelNewsResponse(String response) {
        Gson gson = new Gson();
        try {
            JiSuApi_Body jiSuApi_body = gson.fromJson(response, JiSuApi_Body.class);
            return jiSuApi_body;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WeiXinNew handelWeiXinResponse(String response) {
        Gson gson = new Gson();
        try {
            WeiXinNew weiXinNew = gson.fromJson(response, WeiXinNew.class);
            return weiXinNew;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Music handelMusicResponse(String response) {
        Gson gson = new Gson();
        try {
            Music music = gson.fromJson(response, Music.class);
            return music;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WeatherApi handelWeatherResponse(String response) {
        Gson gson = new Gson();
        try {
            WeatherApi weather = gson.fromJson(response, WeatherApi.class);
            return weather;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
