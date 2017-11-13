package coming.example.lkc.bottomnavigationbar.unitl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkc on 2017/10/23.
 */

public class SharedPreferencesUnitl {
    private final static String FIRST_OPEN = "FirstOpen";
    private static final String USERNAME_LOGIN = "username";
    private static final String LOGIN_STATUS = "login status";
    private static final String BING_NAME = "bingname";


    /**
     * 第一次打开APP
     *
     * @param activity 传入的context
     * @param flag     存储的值
     *                 SharedPreferences
     *                 Context类中的getSharedPreferences()方法，传入参数，该参数就为存储文件的名称。
     */
    public static void PutFirstOpen_SharedPreferencesEditor(Activity activity, boolean flag) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(FIRST_OPEN, 0).edit();
        editor.putBoolean(FIRST_OPEN, flag);
        editor.commit();
    }

    public static boolean getFirstOpen_SharedPreferencesEditor(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(FIRST_OPEN, 0);
        return sharedPreferences.getBoolean(FIRST_OPEN, true);
    }


    /**
     * 登录状态
     *
     * @param activity 活动名
     * @param username 用户名
     * @param flag     为真代表登录，为假未登录
     *                 PreferenceManager类中的getDefaultSharedPreferences()接收context，用当前包名作为文件名
     */
    public static void PutLoginstatus_SharedPreferencesEditor(Activity activity, String username, boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putString(USERNAME_LOGIN, username);
        editor.putBoolean(LOGIN_STATUS, flag);
        editor.commit();
    }

    public static String getUsername_SharedPreferencesEditor(Activity activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        return sp.getString(USERNAME_LOGIN, "");
    }

    public static boolean getLoginstatus_SharedPreferencesEditor(Activity activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        return sp.getBoolean(LOGIN_STATUS, false);
    }

    public static void cancelLoginstatus_SharedPreferences(Activity activity) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.clear();
        editor.commit();
    }

    /**
     * @param activity 传入活动名
     * @param url      必应图片
     *                 Activity类中的getPreferences()方法，以当前活动名作为文件名
     *                 Context.MODE_PRIVATE 为私人的数据
     */
    public static void setBingPic_SharedPreferences(Activity activity, String url) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(BING_NAME, url);
        editor.commit();
    }

    public static String getBingPic_SharedPreferences(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(BING_NAME, "");
    }
}
