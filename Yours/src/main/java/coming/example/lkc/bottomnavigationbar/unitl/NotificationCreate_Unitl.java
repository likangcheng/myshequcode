package coming.example.lkc.bottomnavigationbar.unitl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;

/**
 * Created by 李康成 on 2017/12/18.
 * Android8.0通知适配
 */

public class NotificationCreate_Unitl extends ContextWrapper {
    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    private Context context;

    public NotificationCreate_Unitl(Context base) {
        super(base);
        this.context = base;
    }

    public void createNotificationChannel() {
        //构建一个Channel
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            getManager().createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    /**
     * 8.0以上版本适配通知
     *
     * @return
     */
    public Notification.Builder getChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), id)
                    .setContentTitle("你喜不喜欢李钟硕？")
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.nav_head_img))
                    .setStyle(new Notification.BigTextStyle().bigText("李钟硕（Lee Jong Suk），1989年9月14日出生于京" +
                            "畿道龙仁，韩国男演员、模特。2010年3月参演律政剧《检察官公主》，以演员身" +
                            "份正式出道；9月出演浪漫爱情剧《秘密花园》。2012年主演校园剧《学校2013》。2" +
                            "013年6月首次担纲男主角的奇幻爱情剧《听见你的声音》夺得水木剧年度收视冠军，并凭" +
                            "借该剧入围第50届百想艺术大赏电视部门最佳男演员奖；9月出演古装电影《观相》。"))
                    .setContentText("你会怎么看")
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);
        }
        return null;
    }

    /**
     * @param pendingIntent
     * @param title
     * @param bitmap
     * @param content
     * @return
     */
    public Notification.Builder getChannelNotification(PendingIntent pendingIntent, String title, Bitmap bitmap, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), id)
                    .setContentTitle(title)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(bitmap)
                    .setContentText(content)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }
        return null;
    }

    /**
     * SDK25一下老板本通知
     *
     * @return
     */
    public NotificationCompat.Builder getNotification_25(PendingIntent pendingIntent, String title, Bitmap bitmap, String content) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setLargeIcon(bitmap)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
    }

    public NotificationCompat.Builder getNotification_25() {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("你喜不喜欢李钟硕？")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.nav_head_img))
                .setContentText("你会怎么看")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
    }

    public void sendNotification(PendingIntent pendingIntent, String title, Bitmap bitmap, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (pendingIntent, title, bitmap, content).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(pendingIntent, title, bitmap, content).build();
            getManager().notify(1, notification);
        }
    }

    public void sendNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification
                    ().build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25().build();
            getManager().notify(1, notification);
        }
    }
}
