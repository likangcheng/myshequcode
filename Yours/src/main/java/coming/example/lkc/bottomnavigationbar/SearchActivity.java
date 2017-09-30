package coming.example.lkc.bottomnavigationbar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.adapter.Book_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.adapter.Suggest_list_BaseAdapter;
import coming.example.lkc.bottomnavigationbar.dao.Search_History;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;
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
    private Book_rc_Adapter adapter;
    private Suggest_list_BaseAdapter suggest_adapter;
    private CustomDialog dialog;
    private LinearLayout suggest;
    private List<String> suggest_list_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSearch();
        initSuggest();
        initHistour();
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
                search_et.setHint("Search");
            }
        });

        search_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    suggest.setVisibility(View.VISIBLE);
                    search_rc.setVisibility(View.GONE);
                } else {
                    suggest.setVisibility(View.GONE);
                }
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
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    initFousce(search_rc);
                    remove32(suggest_list_data, v.getText().toString());
                    suggest_list_data.add(0, v.getText().toString());
                    suggest_adapter.notifyDataSetChanged();
                    showProgressDialog();
                    Search(v.getText().toString());
                }
                return false;
            }
        });
    }

    private void initHistour() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        String history = sp.getString("History", "");
        Log.d("search", "initHistour: " + history);
        if (!TextUtils.isEmpty(history)) {
            Collections.addAll(suggest_list_data, history.split("\\|"));
        }
    }

    public static void remove32(List<String> list, String target) {
        if (!TextUtils.isEmpty(target)) {
            Iterator<String> iter = list.iterator();
            while (iter.hasNext()) {
                String item = iter.next();
                if (item.equals(target)) {
                    iter.remove();
                }
            }
        }
    }

    private void initSuggest() {
        suggest = (LinearLayout) findViewById(R.id.suggest_search);
        suggest.setVisibility(View.GONE);
        suggest_adapter = new Suggest_list_BaseAdapter(this, suggest_list_data);
        ListView listview = (ListView) findViewById(R.id.search_list);
        listview.setAdapter(suggest_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


    private void initFousce(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        //隐藏输入法
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        search_rc.setVisibility(View.VISIBLE);
    }

    private void initSearch() {
        search_et = (EditText) findViewById(R.id.search_edtx);
        search_cancel = (ImageView) findViewById(R.id.search_cancel);
        search_cancel.setVisibility(View.GONE);
        search_rc = (RecyclerView) findViewById(R.id.search_rc);
        search_rc.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new Book_rc_Adapter();
        search_rc.setAdapter(adapter);
    }

    private void Search(String text) {
        String NewsUrl = "http://route.showapi.com/582-2?showapi_appid=42977&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe"
                + "&key=" + text;
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, "网络状况异常", Toast.LENGTH_SHORT).show();
                            CloseProgressDialog();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final WeiXinNew weiXinNew = Utility.handelWeiXinResponse(newsResponse);
                if (!call.isCanceled()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weiXinNew != null) {
                                if (weiXinNew.showapi_res_body.pagebean.allNum != 0) {
                                    adapter.setBookData(weiXinNew.showapi_res_body.pagebean.contentlist);
                                    search_rc.smoothScrollToPosition(0);
                                } else {
                                    Toast.makeText(SearchActivity.this, "搜索的内容不存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                            CloseProgressDialog();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String history = "";
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).edit();
        for (String s : suggest_list_data) {
            history = history + s + "|";
            Log.d("search", "onDestroy: " + history);
        }
        editor.putString("History", history);
        editor.commit();
    }

    private void CloseProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new CustomDialog(this, R.style.CustomDialog);
            dialog.show();
        }
        dialog.show();
    }
}
