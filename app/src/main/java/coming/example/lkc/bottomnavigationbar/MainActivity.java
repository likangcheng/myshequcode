package coming.example.lkc.bottomnavigationbar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.fragment.Book_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Game_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Home_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Movie_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Music_Fragment;
import coming.example.lkc.bottomnavigationbar.service.MusicService;
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
    private NetWorkReceiver netWorkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

            }

            //选中到选中
            @Override
            public void onTabReselected(int position) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login_User_Activity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
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
        String USERNAME_LOGIN = sp.getString(Login_User_Activity.USERNAME_LOGIN, "");
        if (LOGIN_STATUS) {
            main_login.setText("欢迎：" + USERNAME_LOGIN);
            circleImageView.setImageResource(R.drawable.ww2017719);
            circleImageView.setEnabled(false);
        } else {
            navigationView.getMenu().getItem(4).setVisible(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra(Login_User_Activity.USERNAME_LOGIN);
                    main_login.setText("欢迎：" + username);
                    circleImageView.setImageResource(R.drawable.ww2017719);
                    circleImageView.setEnabled(false);
                    navigationView.getMenu().getItem(4).setVisible(true);
                    LOGIN_STATUS = true;
                }
                break;
            default:
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  //先得到构造器
                        builder.setTitle("提示"); //设置标题
                        builder.setMessage("确实要退出登录？"); //设置内容
                        builder.setIcon(R.drawable.dsq);//设置图标，图片id即可
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    case R.id.nav_call:
                        if (LOGIN_STATUS) {
                            Intent intent = new Intent(MainActivity.this, User_Details_Activity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, Login_User_Activity.class);
                            startActivityForResult(intent, REQUESTCODE);
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
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
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setActiveColor(R.color.cardview_light_background).setBarBackgroundColor(R.color.fill);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_fill, "Home").setInactiveIconResource(R.drawable.home))
                .addItem(new BottomNavigationItem(R.drawable.book_fill, "Book").setInactiveIconResource(R.drawable.book))
                .addItem(new BottomNavigationItem(R.drawable.music_fill, "Music").setInactiveIconResource(R.drawable.music))
                .addItem(new BottomNavigationItem(R.drawable.tv_fill, "Movie&Tv").setInactiveIconResource(R.drawable.tv))
                .addItem(new BottomNavigationItem(R.drawable.gamepad_fill, "Games").setInactiveIconResource(R.drawable.gamepad))
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
        if (new Date().getTime() - lastPressTime < 2000) {
            finish();//结束程序
        } else {
            lastPressTime = new Date().getTime();//重置lastPressTime
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
