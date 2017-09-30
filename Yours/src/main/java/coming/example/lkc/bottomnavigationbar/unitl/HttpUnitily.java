package coming.example.lkc.bottomnavigationbar.unitl;

import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class HttpUnitily {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static Call call;

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        //超时
        OkHttpClient client = builder.build();
        Request request = new Request.Builder().url(address).build();
        //设置一个公用变量，当APP退出时还出现请求的话，直接释放这个call
        call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void sendOkHttpRequesttoJSON(String address, JSONObject jsonObject, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Log.d("wode", "sendOkHttpRequesttoJSON: ");
        String jsonstring = jsonObject.toString();
        Log.d("wode", "sendOkHttpRequesttoJSON: " + jsonstring);
        RequestBody requestBody = RequestBody.create(JSON, jsonstring);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
