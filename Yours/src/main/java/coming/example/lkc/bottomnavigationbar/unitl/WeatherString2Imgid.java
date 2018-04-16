package coming.example.lkc.bottomnavigationbar.unitl;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by 李康成 on 2017/11/7.
 * 文字转图片
 */

public class WeatherString2Imgid {

    public static int string2imgid(String weather) {
        switch (weather) {
            case "晴":
                return R.drawable.weather_00;
            case "多云":
                return R.drawable.weather_02;
            case "晴间多云":
                return R.drawable.weather_02;
            case "少云":
                return R.drawable.weather_01;
            case "阴":
                return R.drawable.weather_01;
            case "有风":
                return R.drawable.weather_16;
            case "大风":
                return R.drawable.weather_08;
            case "龙卷风":
                return R.drawable.weather_09;
            case "飓风":
                return R.drawable.weather_07;
            case "阵雨":
                return R.drawable.weather_13;
            case "雷阵雨":
                return R.drawable.weather_13;
            case "小雨":
                return R.drawable.weather_03;
            case "中雨":
                return R.drawable.weather_04;
            case "大雨":
                return R.drawable.weather_05;
            case "暴雨":
                return R.drawable.weather_14;
            case "雨夹雪":
                return R.drawable.weather_11;
            case "小雪":
                return R.drawable.weather_10;
            case "中雪":
                return R.drawable.weather_12;
            case "大雪":
                return R.drawable.weather_15;
            case "雾霾":
                return R.drawable.weather_06;
            default:
                return R.drawable.weather_17;
        }
    }
}
