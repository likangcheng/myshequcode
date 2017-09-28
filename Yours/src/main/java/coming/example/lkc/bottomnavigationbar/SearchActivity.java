package coming.example.lkc.bottomnavigationbar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import coming.example.lkc.bottomnavigationbar.adapter.Home_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SearchActivity extends AppCompatActivity {
    private EditText search_et;
    private ImageView search_cancel;
    private RecyclerView search_rc;
    private Home_rc_Adapter adapter;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_et = (EditText) findViewById(R.id.search_edtx);
        search_cancel = (ImageView) findViewById(R.id.search_cancel);
        search_cancel.setVisibility(View.GONE);
        search_rc = (RecyclerView) findViewById(R.id.search_rc);
        search_rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Home_rc_Adapter();
        search_rc.setAdapter(adapter);
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
                search_et.setHint("Search");
            }
        });
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search_cancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    search_cancel.setVisibility(View.GONE);
                }
            }
        });

        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(v.getText().toString())) {
                        Toast.makeText(SearchActivity.this, "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        showProgressDialog();
                        Search(v.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void Search(String text) {
        String NewsUrl = "http://api.jisuapi.com/news/search?keyword=" + text + "&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            CloseProgressDialog();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final JiSuApi_Body jiSuApi_body = Utility.handelNewsResponse(newsResponse);
                if (!call.isCanceled()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiSuApi_body != null) {
                                if (jiSuApi_body.status == 0) {
                                    adapter.setDatalist(jiSuApi_body.result.Newslist);
                                } else {
                                    Toast.makeText(SearchActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                            CloseProgressDialog();
                        }
                    });
                }
            }
        });
    }

    private void CloseProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void showProgressDialog() {
        //隐藏输入法
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (dialog == null) {
            dialog = new CustomDialog(this, R.style.CustomDialog);
            dialog.show();
        }
        dialog.show();
    }
}
