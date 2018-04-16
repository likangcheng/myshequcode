package coming.example.lkc.bottomnavigationbar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import coming.example.lkc.bottomnavigationbar.MusicPlayer;
import coming.example.lkc.bottomnavigationbar.receiver.NotifactionReceiver;


/**
 * Created by 李康成 on 2017/12/22.
 * 每隔2小时启动弹出通知的服务，4.3以上失效
 */
public class AutoGetNotifaction extends Service {
    NotifactionReceiver receiver;

    public AutoGetNotifaction() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (startId == 1) {
//            GetWxJSON(startId);
            RegisterReceiver();
            Log.d("wode", "onStartCommand: ");
        }
        Intent intent2receiver = new Intent("coming.example.lkc.bottomnavigationbar.MY_NOTIF_RECEIVER");
        sendBroadcast(intent2receiver);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 60 * 60 * 1000;//1分钟
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoGetNotifaction.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void RegisterReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("coming.example.lkc.bottomnavigationbar.MY_NOTIF_RECEIVER");
        receiver = new NotifactionReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
//        Log.d("wode", "onDestroy: ");
//        Intent i = new Intent(this, AutoGetNotifaction.class);
//        startService(i);
    }
}
