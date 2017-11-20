package coming.example.lkc.bottomnavigationbar;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.f;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.werb.permissionschecker.PermissionChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import coming.example.lkc.bottomnavigationbar.unitl.WeatherString2Imgid;
import coming.example.lkc.bottomnavigationbar.weather.Forecast;
import coming.example.lkc.bottomnavigationbar.weather.Weather;
import coming.example.lkc.bottomnavigationbar.weather.WeatherApi;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends MyBaseActivity {
    private String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationClient mlocationclient;
    private TextView weather_city, weather_time, weather_back, weather_tmp, weather_cond_text,
            weather_aqi, weather_pm25, weather_qlty, weather_sport, weather_carwash, weather_comfort, weather_location;
    private LinearLayout forecast_layout;
    private ImageView weather_backimg;
    private PermissionChecker permissionChecker;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;
    private MyLocationListener listener = new MyLocationListener();
    private final static String BINGURL = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initPermissions();
        initView();
        initBing();
        initSwip();
    }

    private void initBing() {
        getBingPic(BINGURL);
    }

    private void initSwip() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                initlocation();
                mlocationclient.start();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlocationclient.requestLocation();
                getBingPic(BINGURL);
            }
        });
    }

    private void initView() {
        weather_qlty = (TextView) findViewById(R.id.weather_qlty);
        weather_city = (TextView) findViewById(R.id.weather_city);
        weather_time = (TextView) findViewById(R.id.weather_time);
        weather_back = (TextView) findViewById(R.id.weather_back);
        weather_tmp = (TextView) findViewById(R.id.weather_tmp);
        weather_cond_text = (TextView) findViewById(R.id.weather_cond_txt);
        weather_aqi = (TextView) findViewById(R.id.aqi_text);
        weather_pm25 = (TextView) findViewById(R.id.pm25_text);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_weather);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.bule, R.color.orange, R.color.teal);
        forecast_layout = (LinearLayout) findViewById(R.id.forecast_layout);
        weather_sport = (TextView) findViewById(R.id.sport_text);
        weather_carwash = (TextView) findViewById(R.id.car_wash_text);
        weather_comfort = (TextView) findViewById(R.id.comfort_text);
        nestedScrollView = (NestedScrollView) findViewById(R.id.weather_scrollview);
        nestedScrollView.setVisibility(View.GONE);
        weather_location = (TextView) findViewById(R.id.weather_location);
        weather_backimg = (ImageView) findViewById(R.id.weather_backimg);
        weather_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initPermissions() {
        permissionChecker = new PermissionChecker(this);
        if (permissionChecker.isLackPermissions(PERMISSIONS)) {
            permissionChecker.requestPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationclient.stop();
        mlocationclient.unRegisterLocationListener(listener);
        stopService(new Intent(this, f.class));
    }


    /**
     * 百度更新信息监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder stringBuilder = new StringBuilder();
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                stringBuilder.append("GPS定位：").append(bdLocation.getAddrStr());
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                stringBuilder.append("网络定位：").append(bdLocation.getAddrStr());
            }
            weather_city.setText(bdLocation.getCity());
            weather_location.setText(stringBuilder);
            getWeather(bdLocation.getCity());
            switch (bdLocation.getLocType()) {
                case 167:
                    Toast.makeText(WeatherActivity.this, "服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位", Toast.LENGTH_SHORT).show();
                    break;
                case 62:
                    Toast.makeText(WeatherActivity.this, "无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位", Toast.LENGTH_SHORT).show();
                    break;
                case 505:
                    Toast.makeText(WeatherActivity.this, "AK不存在或者非法，请按照说明文档重新申请AK", Toast.LENGTH_SHORT).show();
                    break;
                case 68:
                    Toast.makeText(WeatherActivity.this, "网络连接失败时，查找本地离线定位时对应的返回结果", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * 百度每隔五秒更新一次
     */
    private void initlocation() {
        mlocationclient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        //获取地址
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mlocationclient.setLocOption(option);
        mlocationclient.registerLocationListener(listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (!permissionChecker.hasAllPermissionsGranted(grantResults)) {
                    permissionChecker.showDialog();
                }
                break;
        }
    }

    /**
     * @param city 城市名
     */
    private void getWeather(String city) {
        final String weather_url = "http://guolin.tech/api/weather?cityid=" + city + "&key=749eab7e12ca4b65b181febd113fa84e";
        HttpUnitily.sendOkHttpRequest(weather_url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JsonString = response.body().string();
                final WeatherApi weatherapi = Utility.handelWeatherResponse(JsonString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherapi != null && "ok".equals(weatherapi.weather.get(0).status)) {
                            ShowWeather(weatherapi.weather.get(0));
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败2", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void ShowWeather(Weather weather) {
        weather_time.setText(weather.basic.update.updateTime.split(" ")[1]);
        weather_cond_text.setText(weather.now.more.info);
        weather_tmp.setText(weather.now.temperature + "℃");
        if (weather.aqi != null) {
            weather_aqi.setText(weather.aqi.aqiCity.aqi);
            weather_pm25.setText(weather.aqi.aqiCity.pm25);
            weather_qlty.setText(weather.aqi.aqiCity.qlty);
        }
        forecast_layout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).
                    inflate(R.layout.forecast_item, forecast_layout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            ImageView infoText = (ImageView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setImageDrawable(getResources()
                    .getDrawable(WeatherString2Imgid.string2imgid(forecast.more.info)));
            maxText.setText(forecast.temperature.max + "℃");
            minText.setText(forecast.temperature.min + "℃");
            forecast_layout.addView(view);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        weather_comfort.setText(comfort);
        weather_carwash.setText(carWash);
        weather_sport.setText(sport);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    private void getBingPic(String Url) {
        HttpUnitily.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            /**
             *
             * @param call
             * @param response
             * @throws IOException
             * 解析逻辑，提取数组来在解析。存入缓存，加载。
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    String jsonString = jsonArray.get(0).toString();
                    JSONObject jsonObject2imgs = new JSONObject(jsonString);
                    String picstring = jsonObject2imgs.getString("url");
                    final String bingpath = "https://cn.bing.com" + picstring;
//                    SharedPreferencesUnitl.setBingPic_SharedPreferences(WeatherActivity.this, bingpath);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            glideload(bingpath);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void glideload(String bingpath) {
        Glide.with(WeatherActivity.this).load(bingpath).bitmapTransform(new BlurTransformation(WeatherActivity.this, 25),
                new CenterCrop(WeatherActivity.this)).into(weather_backimg);
    }
}
