package coming.example.lkc.bottomnavigationbar;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coming.example.lkc.bottomnavigationbar.dao.Users;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog_lkc;
import coming.example.lkc.bottomnavigationbar.unitl.MD5;

public class Register_User extends AppCompatActivity {
    private EditText username, password, password_2;
    private Button regiset_button;
    private Animation shake;
    private static final String USER_PARTTENR = "^[A-Za-z][A-Za-z1-9_-]+$";
    private Pattern pattern = Pattern.compile(USER_PARTTENR);
    public static final String USERNAME_RESULT = "username";
    public static final String PASSWORD_RESULT = "password";
    private TextInputLayout userinput, passinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__user);
        initToolbar();
        initView();
        //抖动动画
        shake = AnimationUtils.loadAnimation(Register_User.this,
                R.anim.shake);
        regiset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = username.getText().toString();
                String pass_word = password.getText().toString();
                String pass_word_2 = password_2.getText().toString();
                if (!queryUsername(user_name) && pass_word.equals(pass_word_2) && validatePassword(pass_word) && validateUser(user_name)) {
                    //用户名不存在&二次密码一直&密码长度&用户名规则
                    String pass_word_md5 = MD5.md5(pass_word, 2);
                    Users users_updata = new Users();
                    users_updata.setUsername(user_name);
                    users_updata.setPassword(pass_word_md5);
                    users_updata.setPath("");
                    users_updata.save();
                    //存入本地数据库
                    Intent resultintent = new Intent();
                    resultintent.putExtra(USERNAME_RESULT, user_name);
                    resultintent.putExtra(PASSWORD_RESULT, pass_word);
                    Register_User.this.setResult(RESULT_OK, resultintent);
                    //返回值
                    Register_Dialog();
                } else if (queryUsername(user_name)) {
                    username.startAnimation(shake);
                    userinput.setError("用户名已存在");
                } else if (!validateUser(user_name)) {
                    username.startAnimation(shake);
                    userinput.setError("用户名必须字母开头且不含非法字符");
                } else if (!pass_word.equals(pass_word_2)) {
                    password.startAnimation(shake);
                    passinput.setError("密码不一致");
                } else if (!validatePassword(pass_word)) {
                    passinput.setError("密码过于简单");
                    password.startAnimation(shake);
                }
            }
        });
    }

    private void initView() {
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password1);
        password_2 = (EditText) findViewById(R.id.register_password2);
        regiset_button = (Button) findViewById(R.id.register_in);
        userinput = (TextInputLayout) findViewById(R.id.user_input);
        passinput = (TextInputLayout) findViewById(R.id.pass_input);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        toolbar.setTitle("用户注册");
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

    private boolean queryUsername(String user_name) {
        LitePal.getDatabase();
        //查询用户名是否存在。
        int usercount = DataSupport.where("username = ?", user_name).count(Users.class);
        if (usercount > 0) {
            return true;
        } else return false;
    }

    private void Register_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("恭喜你！注册成功！");
        builder.setIcon(R.mipmap.icon_yours);
        builder.setPositiveButton("返回登录界面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    public boolean validateUser(String email) {
        Matcher matcher = pattern.matcher(email);
        Log.i("wode", "validateUser: " + matcher.matches());
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }
}
