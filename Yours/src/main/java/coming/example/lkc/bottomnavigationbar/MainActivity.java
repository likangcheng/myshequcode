package coming.example.lkc.bottomnavigationbar;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.werb.permissionschecker.PermissionChecker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.dao.Users;
import coming.example.lkc.bottomnavigationbar.fragment.Book_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Game_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Home_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Movie_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Music_Fragment;
import coming.example.lkc.bottomnavigationbar.listener.ManiActivity2Fragment;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog_lkc;
import coming.example.lkc.bottomnavigationbar.other_view.UpdataAppCreat;
import coming.example.lkc.bottomnavigationbar.service.AutoGetNotifaction;
import coming.example.lkc.bottomnavigationbar.unitl.ActivityCollector;
import coming.example.lkc.bottomnavigationbar.unitl.Head_Photo_Utility;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.NewGlideEngine;
import coming.example.lkc.bottomnavigationbar.unitl.NotificationCreate_Unitl;
import coming.example.lkc.bottomnavigationbar.unitl.ServiceCollector;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends MyBaseActivity {
    private String Url = "https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/json";
    private Fragment[] fragments = new Fragment[5];
    ;
    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout drawerLayout;
    private CircleImageView circleImageView;
    private TextView main_login;
    private NavigationView navigationView;
    public static final int REQUESTCODE = 2;//返回码
    public boolean LOGIN_STATUS;//登录状态
    public String USERNAME_LOGIN;//登录用户名
    private NetWorkReceiver netWorkReceiver;
    //头像背景
    private ImageView head_backimg, head_backimg_change;
    private ManiActivity2Fragment listentr;//传入Fragment
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

    private File templefile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //权限声明
        permissionChecker = new PermissionChecker(this);
        if (permissionChecker.isLackPermissions(PERMISSIONS)) {
            permissionChecker.requestPermissions();
        }
        setContentView(R.layout.activity_main);
        initFirstStart();//第一次启动跳转闪屏页
        initBottomNavigationBar();//底部导航栏
        initActionBar();//标题栏
        initDrawerLayout();//左边用户栏
        login_status_ok();//登录状态
        initFirstonClick();//底部导航栏第一次点击
        initReceiver();//检测网络状态
        initcircleImageView();//头像点击事件
        //通知
//        initNotifaction();
        initAutoGetNotifaction();
        //检测更新
        initUpdataapp();
    }

    private void initAutoGetNotifaction() {
        String servicename = "coming.example.lkc.bottomnavigationbar.service.AutoGetNotifaction";
        Log.d("wode", "initAutoGetNotifaction: " + ServiceCollector.isServiceRunning(this, servicename));
        //后台服务，判断是否运行，运行则跳过。
        if (!ServiceCollector.isServiceRunning(this, servicename)) {
            Intent intent2AutoService = new Intent(this, AutoGetNotifaction.class);
            startService(intent2AutoService);
        }
    }

    private void initUpdataapp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    initUpdata();//检测更新状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initFirstStart() {
        boolean firstopen = SharedPreferencesUnitl.getFirstOpen_SharedPreferencesEditor(MainActivity.this);
        if (firstopen) {
            Intent intent = new Intent(MainActivity.this, Start_Activity.class);
            startActivity(intent);
        }
    }

    private void initcircleImageView() {
        head_backimg_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPicture();
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LOGIN_STATUS) {
                    CustomDialog_lkc customDialog_lkc = new CustomDialog_lkc(MainActivity.this);
                    customDialog_lkc.setOnDialogMenuClick(new CustomDialog_lkc.OnDialogMenuClick() {
                        @Override
                        public void OnCameraClick(View view) {
                            templefile = new File(Environment.getExternalStorageDirectory().getPath() + "/Yours/File", System.currentTimeMillis() + ".jpg");
                            Head_Photo_Utility.getPicFromCamera(MainActivity.this, templefile);
                        }

                        @Override
                        public void OnAblmClick(View view) {
                            Head_Photo_Utility.getPicFromAlbm(MainActivity.this);
                        }

                        @Override
                        public void OnCancelClick(View view) {

                        }
                    });
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
                        UpdataAppCreat updataAppCreat = new UpdataAppCreat(MainActivity.this);
                        updataAppCreat.setUpdataDialog(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initNotifaction() {
        NotificationCreate_Unitl create_unitl = new NotificationCreate_Unitl(this);
        create_unitl.sendNotification();
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
                .imageEngine(new NewGlideEngine())//加载方式
                .forResult(REQUEST_CODE_CHOOSE);//请求码
    }

    private void initReceiver() {
        //广播
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
            main_login.setText(USERNAME_LOGIN);
            List<Users> usersList = DataSupport.where("username = ?", USERNAME_LOGIN).find(Users.class);
            if (TextUtils.isEmpty(usersList.get(0).getPath())) {
                circleImageView.setImageResource(R.drawable.ww2017719);
                circleImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.nav_head_img));
                //用户头像path是否为空，为空设置为初始化头像，否则设置path路径头像。
            } else {
                Glide.with(this).load(usersList.get(0).getPath()).into(circleImageView);
            }
            if (!TextUtils.isEmpty(usersList.get(0).getBackimgpath())) {
                Glide.with(this).load(usersList.get(0).getBackimgpath())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                        .into(head_backimg);
            }
        } else {
            //登录状态为否，隐藏退出登录按钮，全部初始化。
            navigationView.getMenu().getItem(6).setVisible(false);
            head_backimg_change.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE:
                //接收登录返回内容
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra(Login_User_Activity.USERNAME_LOGIN);
                    main_login.setText(username);
                    List<Users> user = DataSupport.where("username = ?", username).find(Users.class);
                    if (TextUtils.isEmpty(user.get(0).getPath())) {
                        circleImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.nav_head_img));
                    } else {
                        Glide.with(this).load(user.get(0).getPath()).into(circleImageView);
                    }
                    if (!TextUtils.isEmpty(user.get(0).getBackimgpath())) {
                        Glide.with(this).load(user.get(0).getBackimgpath())
                                .transition(new DrawableTransitionOptions().crossFade())
                                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                                .into(head_backimg);
                    }
                    navigationView.getMenu().getItem(6).setVisible(true);
                    head_backimg_change.setVisibility(View.VISIBLE);
                    LOGIN_STATUS = true;
                    USERNAME_LOGIN = username;
                }
                break;
            case REQUEST_CODE_CHOOSE:
                //知乎库照片返回内容
                if (resultCode == RESULT_OK) {
                    pathList = Matisse.obtainPathResult(data);
                    Users userupdate = new Users();
                    userupdate.setBackimgpath(pathList.get(0));
                    userupdate.updateAll("username = ?", USERNAME_LOGIN);
                    Glide.with(this).load(pathList.get(0))
                            .transition(new DrawableTransitionOptions().crossFade())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                            .into(head_backimg);
                }
                break;
            case Head_Photo_Utility.CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, Head_Photo_Utility.authorities, templefile);
                        Head_Photo_Utility.cropPhoto(contentUri, MainActivity.this);
                    } else {
                        Head_Photo_Utility.cropPhoto(Uri.fromFile(templefile), MainActivity.this);
                    }
                }
                break;
            case Head_Photo_Utility.ALBUM_REQUEST_CODE:
                //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Head_Photo_Utility.cropPhoto(uri, MainActivity.this);
                }
                break;
            case Head_Photo_Utility.CROP_REQUEST_CODE:
                if (data != null) {
                    //调用剪裁后返回
                    Bundle bundle = data.getExtras();

                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //也可以进行一些保存、压缩等操作后上传
                    String path = Head_Photo_Utility.saveImage(System.currentTimeMillis() + "crop", image);
                    //设置到ImageView上
                    Users userupdate = new Users();
                    userupdate.setPath(path);
                    userupdate.updateAll("username = ?", USERNAME_LOGIN);
//                    circleImageView.setImageBitmap(image);
                    Glide.with(this).load(path).into(circleImageView);
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
        head_backimg_change = nav_head.findViewById(R.id.head_back_change);
        head_backimg = (ImageView) nav_head.findViewById(R.id.nav_head_backimg);
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
                //Activity重新启动
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
            long click_time = 0;

            @Override
            public void onTabReselected(int position) {
                switch (position) {
                    case 0:
                        if (new Date().getTime() - click_time < 1000) {
                            listentr.setOnRefresh();
                        } else {
                            click_time = new Date().getTime();
                        }
                        break;
                }
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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Movie_Fragment) {
            listentr = (ManiActivity2Fragment) fragment;
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
                HttpUnitily.finishAllCall();
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
