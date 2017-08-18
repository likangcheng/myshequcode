package coming.example.lkc.bottomnavigationbar.unitl;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lkc on 2017/7/31.
 */
public class HttpUnitily {
        public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
            OkHttpClient client=new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            client.newCall(request).enqueue(callback);
        }
}
