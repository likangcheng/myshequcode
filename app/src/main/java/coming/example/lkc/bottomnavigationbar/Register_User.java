package coming.example.lkc.bottomnavigationbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import coming.example.lkc.bottomnavigationbar.dao.Users;

public class Register_User extends AppCompatActivity {
    private EditText username, password, password_2;
    private Button regiset_button;
    private Animation shake;
    public static final String USERNAME_RESULT = "username";
    public static final String PASSWORD_RESULT = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__user);
        initToolbar();
        initView();
        shake = AnimationUtils.loadAnimation(Register_User.this,
                R.anim.shake);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("wode", "beforeTextChanged:CharSequence " + s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("wode", "onTextChanged:CharSequence " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("wode", "afterTextChanged:Editable " + s);
//                String user_name = username.getText().toString();
                if (queryUsername(s.toString())) {
                    Toast.makeText(Register_User.this, "用户名已被使用", Toast.LENGTH_SHORT).show();
                    username.startAnimation(shake);
                }
            }
        });
        regiset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = username.getText().toString();
                String pass_word = password.getText().toString();
                String pass_word_2 = password_2.getText().toString();
                if (!queryUsername(user_name) && pass_word.equals(pass_word_2) && pass_word.length() > 5) {
                    Users users_updata = new Users();
                    users_updata.setUsername(user_name);
                    users_updata.setPassword(pass_word);
                    users_updata.save();
                    Intent resultintent = new Intent();
                    resultintent.putExtra(USERNAME_RESULT, user_name);
                    resultintent.putExtra(PASSWORD_RESULT, pass_word);
                    Register_User.this.setResult(RESULT_OK, resultintent);
                    Register_Dialog();
                } else if (queryUsername(user_name)) {
                    username.startAnimation(shake);
                    Toast.makeText(Register_User.this, "用户名已使用", Toast.LENGTH_SHORT).show();
                } else if (!pass_word.equals(pass_word_2)) {
                    password.startAnimation(shake);
                    Toast.makeText(Register_User.this, "密码不一致", Toast.LENGTH_SHORT).show();
                } else if (pass_word.length() <= 5) {
                    password.startAnimation(shake);
                    Toast.makeText(Register_User.this, "密码过于简单", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password1);
        password_2 = (EditText) findViewById(R.id.register_password2);
        regiset_button = (Button) findViewById(R.id.register_in);
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
        List<Users> users = DataSupport.where("username = ?", user_name).find(Users.class);
        if (users.size() > 0) {
            return true;
        } else if (users == null) {
            return false;
        } else return false;
    }

    private void Register_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("注册成功"); //设置内容
        builder.setIcon(R.drawable.cjq);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();//关闭dialog
            }
        });
        builder.create();
        builder.show();
    }
}
