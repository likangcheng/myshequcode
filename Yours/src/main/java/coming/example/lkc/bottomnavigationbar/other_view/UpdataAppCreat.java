package coming.example.lkc.bottomnavigationbar.other_view;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import org.json.JSONObject;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.unitl.CProgressDialogUtils;
import coming.example.lkc.bottomnavigationbar.unitl.UpdateAppHttpUtil;

/**
 * Created by lkc on 2017/9/20.
 */
public class UpdataAppCreat {
    private Activity context;
    String UpdateUrl = "https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/json";
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Yours";

    public UpdataAppCreat(Activity context) {
        this.context = context;
    }

    public void setUpdataDialog(final boolean flag) {
        new UpdateAppManager
                .Builder()
                .setActivity(context)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //设置请求方式 默认get,
                .setPost(false)
                //更新地址
                .setUpdateUrl(UpdateUrl)
                //设置头部
                .setTopPic(R.mipmap.top_3)
                .setTargetPath(path)
                //设置主题色
                .setThemeColor(0xff63B8FF)
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        Log.d("wode", "hasNewApp: ");
                        updateAppManager.showDialogFragment();
                    }

                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String thisVersionName = getVersionName();
                            String newVersionName = jsonObject.getString("new_version");
                            String update = TextUtils.equals(thisVersionName, newVersionName) ? "No" : "Yes";
                            updateAppBean
                                    //是否更新Yes,No
                                    .setUpdate(update)
                                    //新版本号
                                    .setNewVersion(newVersionName)
                                    //下载地址
                                    .setApkFileUrl(jsonObject.getString("apk_file_url"))
                                    //大小
                                    .setTargetSize(jsonObject.getString("target_size"))
                                    //更新内容
                                    .setUpdateLog(jsonObject.getString("update_log"))
                                    //是否强制更新
                                    .setConstraint(jsonObject.getBoolean("constraint"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void onAfter() {
                        if (flag) {
                            CProgressDialogUtils.cancelProgressDialog(context);
                        }
                    }

                    @Override
                    protected void noNewApp() {
                        Toast.makeText(context, "没有新版本", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onBefore() {
                        if (flag) {
                            CProgressDialogUtils.showProgressDialog(context);
                        }
                    }
                });
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
