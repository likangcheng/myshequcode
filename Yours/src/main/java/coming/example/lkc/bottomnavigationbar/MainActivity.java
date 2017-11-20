package coming.example.lkc.bottomnavigationbar;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.werb.permissionschecker.PermissionChecker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.dao.Users;
import coming.example.lkc.bottomnavigationbar.fragment.Book_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Game_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Home_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Movie_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Music_Fragment;
import coming.example.lkc.bottomnavigationbar.other_view.UpdataAppCreat;
import coming.example.lkc.bottomnavigationbar.unitl.ActivityCollector;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends MyBaseActivity {
    private String Url = "https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/json";
    private Fragment[] fragments;
    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout drawerLayout;
    private CircleImageView circleImageView;
    private TextView main_login;
    private NavigationView navigationView;
    public static final int REQUESTCODE = 2;//返回码
    public boolean LOGIN_STATUS;//登录状态
    public String USERNAME_LOGIN;//登录用户名
    private NetWorkReceiver netWorkReceiver;
    private Dialog dialog;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private List<String> pathList;
    private PermissionChecker permissionChecker;
    private String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //权限声明
        permissionChecker = new PermissionChecker(this);
        if (permissionChecker.isLackPermissions(PERMISSIONS)) {
            permissionChecker.requestPermissions();
        }
        fragments = new Fragment[5];
        setContentView(R.layout.activity_main);
        initFirstStart();//第一次启动跳转闪屏页
        initBottomNavigationBar();//底部导航栏
        initActionBar();//标题栏
        initDrawerLayout();//左边用户栏
        login_status_ok();//登录状态
        initFirstonClick();//底部导航栏第一次点击
        initReceiver();//检测网络状态
        initUpdata();//检测更新状态
        initcircleImageView();//头像
    }

    private void initFirstStart() {
        boolean firstopen = SharedPreferencesUnitl.getFirstOpen_SharedPreferencesEditor(MainActivity.this);
        if (firstopen) {
            Intent intent = new Intent(MainActivity.this, Start_Activity.class);
            startActivity(intent);
        }
    }

    private void initcircleImageView() {
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

    //启动页面检测是否有更新
    private void initUpdata() {
        HttpUnitily.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String newVersionName = jsonObject.getString("new_version");
                    PackageManager packageManager = getPackageManager();
                    // getPackageName()是你当前类的包名，0代表是获取版本信息
                    PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                    String version = packInfo.versionName;
                    //当前版本和后台版本不匹配，弹出通知
                    if (!TextUtils.equals(version, newVersionName)) {
                        initNotifaction();//通知
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initNotifaction() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("APP有新的版本了！")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("系统检测到Yours有新的版本啦！可以在用户栏版本更新处进行下载更新。"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_yours)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.gx_icon))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
        manager.notify(1, notification);
    }

    //打开图片选择器  知乎库
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
        //广播
        /**
         *
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkReceiver = new NetWorkReceiver();
        registerReceiver(netWorkReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);//关闭广播

    }

    private void login_status_ok() {
        //本地配置文件查询登录状态，登录用户名
        LOGIN_STATUS = SharedPreferencesUnitl.getLoginstatus_SharedPreferencesEditor(MainActivity.this);
        USERNAME_LOGIN = SharedPreferencesUnitl.getUsername_SharedPreferencesEditor(MainActivity.this);
        if (LOGIN_STATUS) {
            main_login.setText("欢迎：" + USERNAME_LOGIN);
            List<Users> usersList = DataSupport.where("username = ?", USERNAME_LOGIN).find(Users.class);
            if (TextUtils.isEmpty(usersList.get(0).getPath())) {
//                Log.d("wode", "login_status_ok: no picture" + usersList.get(0).getPath());
                circleImageView.setImageResource(R.drawable.ww2017719);
                //用户头像path是否为空，为空设置为初始化头像，否则设置path路径头像。
            } else {
                Glide.with(this).load(usersList.get(0).getPath()).into(circleImageView);
            }
        } else {
            //登录状态为否，隐藏退出登录按钮，全部初始化。
            navigationView.getMenu().getItem(6).setVisible(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE:
                //接收登录返回内容
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra(Login_User_Activity.USERNAME_LOGIN);
                    main_login.setText("欢迎：" + username);
                    List<Users> user = DataSupport.where("username = ?", username).find(Users.class);
                    if (TextUtils.isEmpty(user.get(0).getPath())) {
                        circleImageView.setImageResource(R.drawable.ww2017719);
                    } else {
                        Glide.with(this).load(user.get(0).getPath()).into(circleImageView);
                    }
                    navigationView.getMenu().getItem(6).setVisible(true);
                    LOGIN_STATUS = true;
                    USERNAME_LOGIN = username;
                }
                break;
            case REQUEST_CODE_CHOOSE:
                //照片返回内容
                if (resultCode == RESULT_OK) {
                    pathList = Matisse.obtainPathResult(data);
                    Users userupdate = new Users();
                    userupdate.setPath(pathList.get(0));
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
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);
        navigationView.setItemIconTintList(null);
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
                        UpdataAppCreat updataAppCreat = new UpdataAppCreat(MainActivity.this);
                        updataAppCreat.setUpdataDialog(true);
                        break;
                    case R.id.nav_collection_song:
                        boolean flag = SharedPreferencesUnitl.getLoginstatus_SharedPreferencesEditor(MainActivity.this);
                        if (flag) {
                            Intent intent2collection = new Intent(MainActivity.this, CollectionActivity.class);
                            startActivity(intent2collection);
                        } else {
                            Intent intent2login = new Intent(MainActivity.this, Login_User_Activity.class);
                            startActivityForResult(intent2login, REQUESTCODE);
                        }
                        break;
                    case R.id.nav_aboutversion:
                        Intent intent2aboutversion = new Intent(MainActivity.this, About_Version_Activity.class);
                        startActivity(intent2aboutversion);
                        break;
                    case R.id.nav_weather:
                        Intent intent2weather = new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(intent2weather);
                        break;
                    default:
                        break;
                }
                return true;
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
                SharedPreferencesUnitl.cancelLoginstatus_SharedPreferences(MainActivity.this);
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
    private void initFirstonClick() {
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
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setActiveColor(R.color.wirte_dark).setBarBackgroundColor(R.color.colorAccent);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_fill, R.string.home_text).setInactiveIconResource(R.drawable.home))
                .addItem(new BottomNavigationItem(R.drawable.book_fill, R.string.article_text).setInactiveIconResource(R.drawable.book))
                .addItem(new BottomNavigationItem(R.drawable.music_fill, R.string.music_text).setInactiveIconResource(R.drawable.music))
                .addItem(new BottomNavigationItem(R.drawable.toutiao_fill, R.string.headline_text).setInactiveIconResource(R.drawable.toutiao))
                .addItem(new BottomNavigationItem(R.drawable.tiyu_fill, R.string.game_text).setInactiveIconResource(R.drawable.tiyu))
                .setFirstSelectedPosition(0)
                .initialise();
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

            }

            //选中到选中
            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void initFragments(int position) {
        switch (position) {
            case 0:
                fragments[0] = new Movie_Fragment();
                break;
            case 1:
                fragments[1] = new Book_Fragment();
                break;
            case 2:
                fragments[2] = new Music_Fragment();
                break;
            case 3:
                fragments[3] = new Home_Fragment();
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
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.delete:

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
                ActivityCollector.finishAll();
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
