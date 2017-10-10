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
import android.view.MotionEvent;
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
    private TextView search_back, histour_header;
    private ListView listview;
    private Button histour_cancel;
    private boolean NO_HISITOUR = false;
    private InputMethodManager imManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imManager = (InputMethodManager) getSystemService(SearchActivity.this.INPUT_METHOD_SERVICE);
        initSearch();//声明Search相关变量
        initSuggest();//历史记录相关变量
        initHistour();//获取历史记录
        initSearchListen();//搜索栏相关的事件监听
        initBack();//取消返回键
        initCancelHistour();//清空历史搜索
    }

    private void initCancelHistour() {
        histour_cancel = (Button) findViewById(R.id.cancel_histour);
        histour_header = (TextView) findViewById(R.id.header);
        if (suggest_list_data.size() == 0 || suggest_list_data == null) {
            histour_cancel.setVisibility(View.GONE);
            histour_header.setVisibility(View.GONE);
            NO_HISITOUR = true;
        }
        histour_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest_list_data.clear();
                suggest_adapter.notifyDataSetChanged();
                histour_cancel.setVisibility(View.GONE);
                histour_header.setVisibility(View.GONE);
                NO_HISITOUR = true;
            }
        });
    }

    private void initBack() {
        search_back = (TextView) findViewById(R.id.search_back);
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSearchListen() {
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
                search_et.setHint("Search");
            }
        });//点击清空

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
        });//搜索栏获取焦点显示历史搜索
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
        });//监听搜索栏上内容，根据内容显示cancel项
        search_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_et.setFocusable(true);
                search_et.setFocusableInTouchMode(true);
                search_et.requestFocus();
                imManager.showSoftInput(search_et,InputMethodManager.SHOW_FORCED);
                return true;
            }
        });
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    initFousce(search_rc);//搜索结果获取焦点
                    remove32(suggest_list_data, v.getText().toString());//取消历史搜索相同搜索
                    suggest_list_data.add(0, v.getText().toString());//增加此次搜索
                    suggest_adapter.notifyDataSetChanged();
                    //有搜索记录则显示出来
                    if (NO_HISITOUR) {
                        histour_header.setVisibility(View.VISIBLE);
                        histour_cancel.setVisibility(View.VISIBLE);
                        NO_HISITOUR = false;
                    }
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
        //本地获取History
        if (!TextUtils.isEmpty(history)) {
            //加入list
            Collections.addAll(suggest_list_data, history.split("\\|"));
        }
    }

    public static void remove32(List<String> list, String target) {
        //功能，删除list中内容重复的项
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
        //历史记录提示
        suggest = (LinearLayout) findViewById(R.id.suggest_search);
        suggest.setVisibility(View.GONE);
        suggest_adapter = new Suggest_list_BaseAdapter(this, suggest_list_data, listview);
        listview = (ListView) findViewById(R.id.search_list);
        listview.setAdapter(suggest_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initFousce(search_rc);
                showProgressDialog();
                Search(suggest_list_data.get(position));
            }
        });
    }


    private void initFousce(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        //隐藏输入法
        imManager.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
        //搜索栏失去焦点
        search_et.setFocusable(false);
        search_et.setFocusableInTouchMode(false);
        search_et.requestFocus();
        suggest.setVisibility(View.GONE);
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
                            }else {
                                //获取对象为空，一般是网络可以访问，但已被拦截，而且能够获取到JSON返回值，但是值乱码所以JSon序列化
                                //会失效。
                                Toast.makeText(SearchActivity.this, "请检测网络是否连接异常", Toast.LENGTH_SHORT).show();
                                finish();
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
            //遍历list存入history，转存于本地
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