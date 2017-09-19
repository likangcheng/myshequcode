package coming.example.lkc.bottomnavigationbar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.AppUpdateUtils;
import com.werb.permissionschecker.PermissionChecker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coming.example.lkc.bottomnavigationbar.dao.Users;
import coming.example.lkc.bottomnavigationbar.fragment.Book_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Game_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Home_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Movie_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Music_Fragment;
import coming.example.lkc.bottomnavigationbar.service.MusicService;
import coming.example.lkc.bottomnavigationbar.unitl.CProgressDialogUtils;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.UpdateAppHttpUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Fragment[] fragments;
    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout drawerLayout;
    private CircleImageView circleImageView;
    private TextView main_login;
    private NavigationView navigationView;
    public static final int REQUESTCODE = 2;
    public boolean LOGIN_STATUS;
    public String USERNAME_LOGIN;
    private NetWorkReceiver netWorkReceiver;
    private Dialog dialog;
    private BadgeItem badgeItem;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private List<String> pathList;
    private PermissionChecker permissionChecker;
    private String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionChecker = new PermissionChecker(this);
        if (permissionChecker.isLackPermissions(PERMISSIONS)) {
            permissionChecker.requestPermissions();
        }
        setContentView(R.layout.activity_main);
        initBottomNavigationBar();
        initActionBar();
        initDrawerLayout();
        login_status_ok();
        fragments = new Fragment[5];
        initonClick();
        initReceiver();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            //未选中到选中
            @Override
            public void onTabSelected(int position) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                hideAllFragments(ft);
                if (fragments[position] == null) {
                    initFragments(position);
                    ft.add(R.id.frameLayout_fragment, fragments[position]);
                } else {
                    ft.show(fragments[position]);
                }
                ft.commit();
            }

            //选中到未选中
            @Override
            public void onTabUnselected(int position) {
                if (position == 4) {
                    badgeItem.hide();
                    Log.d("wode", "onTabSelected: ");
                }
            }

            //选中到选中
            @Override
            public void onTabReselected(int position) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LOGIN_STATUS) {
                    initPicture();
                } else {
                    Intent intent = new Intent(MainActivity.this, Login_User_Activity.class);
                    startActivityForResult(intent, REQUESTCODE);
                }
            }
        });
    }

    private void initPicture() {
        Matisse
                .from(MainActivity.this)
                .choose(MimeType.ofImage())//照片视频全部显示
                .countable(true)//有序选择图片
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "coming.example.lkc.bottomnavigationbar.fileprovider"))
                .maxSelectable(1)//最大选择数量为9
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new GlideEngine())//加载方式
                .forResult(REQUEST_CODE_CHOOSE);//请求码
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkReceiver = new NetWorkReceiver();
        registerReceiver(netWorkReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);

    }

    private void login_status_ok() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        LOGIN_STATUS = sp.getBoolean(Login_User_Activity.LOGIN_STATUS, false);
        USERNAME_LOGIN = sp.getString(Login_User_Activity.USERNAME_LOGIN, "");
        if (LOGIN_STATUS) {
            main_login.setText("欢迎：" + USERNAME_LOGIN);
            List<Users> usersList = DataSupport.where("username = ?", USERNAME_LOGIN).find(Users.class);
            if (TextUtils.isEmpty(usersList.get(0).getPath())) {
                Log.d("wode", "login_status_ok: no picture" + usersList.get(0).getPath());
                circleImageView.setImageResource(R.drawable.ww2017719);
            } else {
                Glide.with(this).load(usersList.get(0).getPath()).into(circleImageView);
            }
        } else {
            navigationView.getMenu().getItem(3).setVisible(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra(Login_User_Activity.USERNAME_LOGIN);
                    main_login.setText("欢迎：" + username);
                    List<Users> user = DataSupport.where("username = ?", username).find(Users.class);
                    if (TextUtils.isEmpty(user.get(0).getPath())) {
                        circleImageView.setImageResource(R.drawable.ww2017719);
                    } else {
                        Glide.with(this).load(user.get(0).getPath()).into(circleImageView);
                    }
                    navigationView.getMenu().getItem(3).setVisible(true);
                    LOGIN_STATUS = true;
                    USERNAME_LOGIN = username;
                }
                break;
            case REQUEST_CODE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    pathList = Matisse.obtainPathResult(data);
                    Users userupdate = new Users();
                    userupdate.setPath(pathList.get(0));
                    Log.d("wode", "onActivityResult: " + pathList.get(0));
                    userupdate.updateAll("username = ?", USERNAME_LOGIN);
                    Glide.with(this).load(pathList.get(0)).into(circleImageView);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (!permissionChecker.hasAllPermissionsGranted(grantResults)) {
                    permissionChecker.showDialog();
                } else {
                }
                break;
        }
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_head = navigationView.getHeaderView(0);
        circleImageView = (CircleImageView) nav_head.findViewById(R.id.icon_image);
        main_login = (TextView) nav_head.findViewById(R.id.maim_login);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_Logout:
                        initTCDLdialog();
                        break;
                    case R.id.nav_frends:
                        Intent intent = new Intent(MainActivity.this, JBS_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_mail:
                        initUpdataAPP();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initUpdataAPP() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Map<String, String> params = new HashMap<String, String>();
        params.put("appVersion", AppUpdateUtils.getVersionName(this));
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                        //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                        //设置请求方式 默认get,
                .setPost(false)
                .setParams(params)
                        //更新地址
                .setUpdateUrl("https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/json")
                        //设置头部
                .setTopPic(R.mipmap.top_3)
                .setTargetPath(path)
                        //设置主题色
                .setThemeColor(0xff6392ff)
                .build()
                        //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            updateAppBean
                                    //是否更新Yes,No
                                    .setUpdate(jsonObject.getString("update"))
                                            //新版本号
                                    .setNewVersion(jsonObject.getString("new_version"))
                                            //下载地址
                                    .setApkFileUrl(jsonObject.getString("apk_file_url"))
                                            //大小
                                    .setTargetSize(jsonObject.getString("target_size"))
                                            //更新内容
                                    .setUpdateLog(jsonObject.getString("update_log"))
                                            //是否强制更新
                                    .setConstraint(jsonObject.getBoolean("constraint"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void onAfter() {
                        CProgressDialogUtils.cancelProgressDialog(MainActivity.this);
                    }

                    @Override
                    protected void noNewApp() {
                        Toast.makeText(MainActivity.this, "没有新版本", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onBefore() {
                        CProgressDialogUtils.showProgressDialog(MainActivity.this);
                    }
                });
    }

    private void initTCDLdialog() {
        dialog = new Dialog(this, R.style.PhotographAndPicture_DialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.determine_cancel_dialog_layout, null);
        TextView ok = (TextView) view.findViewById(R.id.tcdl_ok);
        TextView cancel = (TextView) view.findViewById(R.id.tcdl_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(MainActivity.this).edit();
                editor.clear();
                editor.apply();
//                              //Activity重新启动
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    //加载页面时第一次点击
    private void initonClick() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        initFragments(0);
        ft.add(R.id.frameLayout_fragment, fragments[0]);
        ft.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i] != null) {
                ft.hide(fragments[i]);
            }
        }
    }

    private void initActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.gengduo);
        }
    }

    private void initBottomNavigationBar() {
        badgeItem = new BadgeItem()
                .setBorderWidth(2)
                .setBorderColorResource(R.color.red)
                .setBackgroundColorResource(R.color.red)
                .setHideOnSelect(true)
                .setText("3");
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setActiveColor(R.color.wirte_dark).setBarBackgroundColor(R.color.fill);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_fill, "Home").setInactiveIconResource(R.drawable.home))
                .addItem(new BottomNavigationItem(R.drawable.book_fill, "Book").setInactiveIconResource(R.drawable.book))
                .addItem(new BottomNavigationItem(R.drawable.music_fill, "Music").setInactiveIconResource(R.drawable.music))
                .addItem(new BottomNavigationItem(R.drawable.tv_fill, "Movie").setInactiveIconResource(R.drawable.tv))
                .addItem(new BottomNavigationItem(R.drawable.gamepad_fill, "Games").setBadgeItem(badgeItem).setInactiveIconResource(R.drawable.gamepad))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    private void initFragments(int position) {
        switch (position) {
            case 0:
                fragments[0] = new Home_Fragment();
                break;
            case 1:
                fragments[1] = new Book_Fragment();
                break;
            case 2:
                fragments[2] = new Music_Fragment();
                break;
            case 3:
                fragments[3] = new Movie_Fragment();
                break;
            case 4:
                fragments[4] = new Game_Fragment();
                break;
            default:
                throw new NullPointerException();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    long lastPressTime = 0;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            if (new Date().getTime() - lastPressTime < 2000) {
                HttpUnitily.call.cancel();
                finish();//结束程序
            } else {
                lastPressTime = new Date().getTime();//重置lastPressTime
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class NetWorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isAvailable()) {

            } else {
                Toast.makeText(context, "网络不可用，请打开网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
