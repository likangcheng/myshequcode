package coming.example.lkc.bottomnavigationbar;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.litepal.LitePal;

/**
 * Created by lkc on 2017/9/4.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }


    public static Context getContext() {
        return context;
    }
}
