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

    //第一次打开APP
    public static void PutFirstOpen_SharedPreferencesEditor(Activity activity, boolean flag) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(FIRST_OPEN, 0).edit();
        editor.putBoolean(FIRST_OPEN, flag);
        editor.commit();
    }

    public static boolean getFirstOpen_SharedPreferencesEditor(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(FIRST_OPEN, 0);
        return sharedPreferences.getBoolean(FIRST_OPEN, true);
    }

    //登录状态
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
}
