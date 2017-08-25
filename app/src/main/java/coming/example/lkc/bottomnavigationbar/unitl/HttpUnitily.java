package coming.example.lkc.bottomnavigationbar.unitl;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by lkc on 2017/7/31.
 */
public class HttpUnitily {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequesttoJSON(String address, JSONObject jsonObject, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Log.d("wode", "sendOkHttpRequesttoJSON: ");
        String jsonstring = jsonObject.toString();
        Log.d("wode", "sendOkHttpRequesttoJSON: "+jsonstring);
        RequestBody requestBody = RequestBody.create(JSON, jsonstring);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
