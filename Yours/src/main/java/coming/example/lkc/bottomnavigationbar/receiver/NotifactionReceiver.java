package coming.example.lkc.bottomnavigationbar.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import coming.example.lkc.bottomnavigationbar.Book_Card_Activity;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;
import coming.example.lkc.bottomnavigationbar.dao.WeiXin_Content_list;
import coming.example.lkc.bottomnavigationbar.service.AutoGetNotifaction;
import coming.example.lkc.bottomnavigationbar.unitl.GlideApp;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.NotificationCreate_Unitl;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotifactionReceiver extends BroadcastReceiver {
    private static final String WXURL = "http://route.showapi.com/582-2?showapi_appid=42977" +
            "&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe";
    private PendingIntent pendingIntent;
    private WeiXinNew weiXinNew;
    private int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        GetWxJSON(context);

    }

    private void GetWxJSON(final Context context) {
        HttpUnitily.sendOkHttpRequest(WXURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String request = response.body().string();
                weiXinNew = Utility.handelWeiXinResponse(request);
                WeiXin_Content_list weiXinContentList = weiXinNew.showapi_res_body.pagebean.contentlist.get(0);
                Intent i = new Intent(context, Book_Card_Activity.class);
                i.putExtra(Book_Card_Activity.WEIXIN_DATA, weiXinContentList);
                pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                String title = weiXinContentList.weixintitle;
                String content = weiXinContentList.infourl;
                NotificationCreate_Unitl unitl = new NotificationCreate_Unitl(context);
                try {
                    Bitmap bitmap = GlideApp.with(context).asBitmap().load(weiXinContentList.contentImg).into(120, 120).get();
                    unitl.sendNotification(pendingIntent, title, bitmap, content);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
