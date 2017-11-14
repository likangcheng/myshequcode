package coming.example.lkc.bottomnavigationbar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.adapter.Search_TablayoutAndFragment_Adapter;
import coming.example.lkc.bottomnavigationbar.adapter.Suggest_list_BaseAdapter;
import coming.example.lkc.bottomnavigationbar.fragment.Search_Music_Fragment;
import coming.example.lkc.bottomnavigationbar.fragment.Search_WeiXin_Fragment;
import coming.example.lkc.bottomnavigationbar.listener.Search2Fragment;
import coming.example.lkc.bottomnavigationbar.unitl.Logwrite;


public class SearchActivity extends AppCompatActivity {
    private EditText search_et;
    private ImageView search_cancel;
    //    private Search2Fragment listener1, listener2;
    private Suggest_list_BaseAdapter suggest_adapter;
    private LinearLayout suggest, search_layout;
    private List<String> suggest_list_data = new ArrayList<>();
    private TextView search_back, histour_header;
    private ListView listview;
    private Search_Music_Fragment music_fragment = new Search_Music_Fragment();
    private Search_WeiXin_Fragment weiXin_fragment = new Search_WeiXin_Fragment();
    private Button histour_cancel;
    private boolean NO_HISITOUR = false;
    private InputMethodManager imManager;
    private ViewPager viewpager;
    private Search_TablayoutAndFragment_Adapter fragmentadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //软键盘
        imManager = (InputMethodManager) getSystemService(SearchActivity.this.INPUT_METHOD_SERVICE);
        initSearch();//声明Search相关变量
        initSearch_FragmentViewPager();//搜索Fragment viewpager分类搜索
        initSuggest();//历史记录相关变量
        initHistour();//获取历史记录
        initSearchListen();//搜索栏相关的事件监听
        initBack();//取消返回键
        initCancelHistour();//清空历史搜索
    }

    private void initSearch_FragmentViewPager() {
        final TabLayout tablayout = (TabLayout) findViewById(R.id.search_tablayout);
        viewpager = (ViewPager) findViewById(R.id.search_viewpager);
        List<String> tabtitle = new ArrayList<>(Arrays.asList("微信", "音乐"));
        List<Fragment> fragmentlist = new ArrayList<>();
        fragmentlist.add(weiXin_fragment);
        fragmentlist.add(music_fragment);
        fragmentadapter =
                new Search_TablayoutAndFragment_Adapter(getSupportFragmentManager(), tabtitle, fragmentlist);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(fragmentadapter);
    }

    /**
     * @Override public void onAttachFragment(Fragment fragment) {
     * super.onAttachFragment(fragment);
     * Log.d("wode", "onAttachFragment:1 ");
     * //调用方法将listener传入Fragment实现通信
     * if (fragment instanceof Search_WeiXin_Fragment) {
     * try {
     * Log.d("wode", "onAttachFragment:2 ");
     * listener1 = (Search2Fragment) fragment;
     * } catch (Exception e) {
     * Log.d("wode", "onAttachFragment: 3");
     * e.printStackTrace();
     * }
     * }
     * if (fragment instanceof Search_Music_Fragment) {
     * try {
     * listener2 = (Search2Fragment) fragment;
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     * }
     **/

    private void initCancelHistour() {
        histour_cancel = (Button) findViewById(R.id.cancel_histour);
        histour_header = (TextView) findViewById(R.id.header);
        //初始化判断是否为空
        if (suggest_list_data.size() == 0 || suggest_list_data == null) {
            histour_cancel.setVisibility(View.GONE);
            histour_header.setVisibility(View.GONE);
            NO_HISITOUR = true;
        }
        //历史记录清除
        histour_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest_list_data.clear();
                suggest_adapter.notifyDataSetChanged();
                histour_cancel.setVisibility(View.GONE);
                histour_header.setVisibility(View.GONE);
                NO_HISITOUR = true;
                Toast.makeText(SearchActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBack() {
        //取消返回
        search_back = (TextView) findViewById(R.id.search_back);
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imManager.isActive()) {
                    imManager.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
                }
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
                    search_layout.setVisibility(View.GONE);
                    imManager.showSoftInput(search_et, InputMethodManager.SHOW_FORCED);
                } else {
                    search_layout.setVisibility(View.VISIBLE);
                    suggest.setVisibility(View.GONE);
                    //隐藏输入法
                    imManager.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
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
        });

        //监听搜索栏上内容，根据内容显示cancel项
/**        search_et.setOnClickListener(new View.OnClickListener() {
@Override public void onClick(View view) {
search_et.setFocusable(true);
search_et.setFocusableInTouchMode(true);
search_et.requestFocus();
imManager.showSoftInput(search_et, InputMethodManager.SHOW_FORCED);
}
});*/
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String search_content = v.getText().toString();
                if (!TextUtils.isEmpty(search_content)) {
                    if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        initFousce();//搜索结果获取焦点
                        remove32(suggest_list_data, search_content);//取消历史搜索相同搜索
                        suggest_list_data.add(0, search_content);//增加此次搜索
                        suggest_adapter.notifyDataSetChanged();
                        //有搜索记录则显示出来
                        if (NO_HISITOUR) {
                            histour_header.setVisibility(View.VISIBLE);
                            histour_cancel.setVisibility(View.VISIBLE);
                            NO_HISITOUR = false;
                        }
                        //上一次搜索结婚清空
                        viewpager.removeAllViews();
                        viewpager.setAdapter(fragmentadapter);
                        weiXin_fragment.SearchString(search_content, SearchActivity.this);
                        music_fragment.SearchString(search_content);
                        Logwrite.LOG("1");
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    return true;
                }
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
                search_et.setText(suggest_list_data.get(position));
                search_et.setSelection(suggest_list_data.get(position).length());
                initFousce();
                viewpager.removeAllViews();
                viewpager.setAdapter(fragmentadapter);
                weiXin_fragment.SearchString(suggest_list_data.get(position), SearchActivity.this);
                music_fragment.SearchString(suggest_list_data.get(position));
            }
        });
    }


    private void initFousce() {
        View view = findViewById(R.id.foucus_search);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        //隐藏输入法
        imManager.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
        //搜索栏失去焦点

/*        search_et.setFocusable(false);
        search_et.setFocusableInTouchMode(false);
        search_et.requestFocus();
        suggest.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
        Logwrite.LOG("setVisibility");*/
    }

    private void initSearch() {
        search_et = (EditText) findViewById(R.id.search_edtx);
        search_cancel = (ImageView) findViewById(R.id.search_cancel);
        search_cancel.setVisibility(View.GONE);
        search_layout = (LinearLayout) findViewById(R.id.search_layout);
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


}
