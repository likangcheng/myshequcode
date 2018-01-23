package coming.example.lkc.bottomnavigationbar;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.dao.Users;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.KeyboardStatusDetector;
import coming.example.lkc.bottomnavigationbar.unitl.MD5;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;

/**
 * Created by lkc on 2017/8/3.
 */
public class Login_User_Activity extends MyBaseActivity {
    private EditText username, password;
    private Button login_register, longin_to;
    public static final int RquestCode = 1;
    public static final String USERNAME_LOGIN = "username";
    public static final String LOGIN_STATUS = "login status";
    private ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user_layout);
        initToolbar();
        initView();
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_User_Activity.this, Register_User.class);
                startActivityForResult(intent, RquestCode);
            }
        });
        longin_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = username.getText().toString();
                String user_password = password.getText().toString();
                if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_password)) {
                    if (queryLogin(user_name, user_password)) {
                        //登陆成功
                        SharedPreferencesUnitl.PutLoginstatus_SharedPreferencesEditor(Login_User_Activity.this, user_name, true);
                        Intent mintent = new Intent();
                        mintent.putExtra(USERNAME_LOGIN, user_name);
                        Login_User_Activity.this.setResult(RESULT_OK, mintent);
                        Register_Dialog();
                    } else {
                        Toast.makeText(Login_User_Activity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else if (TextUtils.isEmpty(user_name) ||
                        TextUtils.isEmpty(user_password)) {
                    Toast.makeText(Login_User_Activity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        longin_to = (Button) findViewById(R.id.login_to);
        login_register = (Button) findViewById(R.id.login_register);
        username = (EditText) findViewById(R.id.login_user);
        password = (EditText) findViewById(R.id.login_password);
        scrollView = (ScrollView) findViewById(R.id.login_scrollview);
        scrollView.setVerticalScrollBarEnabled(false);
        KeyboardStatusDetector keyboardStatusDetector = new KeyboardStatusDetector();
        keyboardStatusDetector.registerActivity(this);
        keyboardStatusDetector.setmVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean keyboardVisible) {
                if (keyboardVisible) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getHeight());
                        }
                    }, 100);

                }
            }
        });
        View view = findViewById(R.id.login_focuse);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle("用户登录");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean queryLogin(String username, String password) {
        String password_md5 = MD5.md5(password, 2);
        List<Users> users = DataSupport.where("username = ?", username).find(Users.class);
        if (users.size() == 0 || users == null) {
            return false;
        } else if (TextUtils.equals(password_md5, users.get(0).getPassword())) {
            return true;
        }
        return false;
    }

    private void Register_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("登录成功"); //设置内容
        builder.setIcon(R.mipmap.icon_yours);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();//关闭dialog
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RquestCode:
                if (resultCode == RESULT_OK) {
                    String username_result = data.getStringExtra(Register_User.USERNAME_RESULT);
                    String password_result = data.getStringExtra(Register_User.PASSWORD_RESULT);
                    username.setText(username_result);
                    password.setText(password_result);
                    password.requestFocus();
                    password.setSelection(password.getText().length());
                }
                break;
            default:
                break;
        }
    }
}
