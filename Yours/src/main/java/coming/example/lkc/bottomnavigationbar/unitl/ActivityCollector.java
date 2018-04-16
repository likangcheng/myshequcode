package coming.example.lkc.bottomnavigationbar.unitl;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李康成 on 2017/11/16.
 * 启动项控制类
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public static Activity queryActivity(String s) {
        for (Activity activity : activities) {
            if (activity.toString().contains(s)) {
                return activity;
            }
        }
        return null;
    }
}
