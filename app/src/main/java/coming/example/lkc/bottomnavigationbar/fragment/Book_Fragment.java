package coming.example.lkc.bottomnavigationbar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Book_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.adapter.EndLessOnScrollListener;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Book_Fragment extends Fragment {
    private RecyclerView bookrecyclerView;
    private SwipeRefreshLayout swip;
    private SpringView springView;
    private WeiXinNew weiXinNew;
    private GridLayoutManager gridLayoutManager;
    private Book_rc_Adapter adapter;
    private LinearLayout networkerro;
    private int page = 1;
//    private boolean REFRESH = false;
//    //已经加载出来的Item的数量
//    private int totalItemCount;
//
//    //主要用来存储上一个totalItemCount
//    private int previousTotal = 0;
//
//    //在屏幕上可见的item数量
//    private int visibleItemCount;
//
//    //在屏幕可见的Item中的第一个
//    private int firstVisibleItem;
//
//    //是否正在上拉数据
//    private boolean loading = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book, null);
        bookrecyclerView = (RecyclerView) view.findViewById(R.id.weixin_rc);
        networkerro = (LinearLayout) view.findViewById(R.id.book_network_erro);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.book_swip);
        springView = (SpringView) view.findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        swip.setColorSchemeResources(R.color.colorAccent, R.color.teal, R.color.orange, R.color.bule);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        bookrecyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
                requestWexinNEWS();
            }
        });
        networkerro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swip.setRefreshing(true);
                requestWexinNEWS();
            }
        });
//        requestWexinNEWS();
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
//                requestWexinNEWS();
            }

            @Override
            public void onLoadmore() {
                swip.setEnabled(false);
                page++;
                loadmoreNEWS();
            }
        });

//        springView.setHeader(new AliHeader(getActivity(), R.drawable.ali, true));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new AliFooter(getActivity(), false));

        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                previousTotal = 0;
//                loading = true;
//                bookrecyclerView.removeAllViews();
                requestWexinNEWS();
            }
        });
//        bookrecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = recyclerView.getChildCount();
//                if (previousTotal == 0) {
//                    totalItemCount = 20;
//                } else {
//                    totalItemCount = gridLayoutManager.getItemCount();
//                }
//                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
//
//
//                if (loading && totalItemCount > previousTotal) {
//
//                    loading = false;
//                    previousTotal = totalItemCount;
//                }
//
//                //这里需要好好理解
//                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
//                    showProgressDialog();
//                    page++;
//                    loadmoreNEWS();
//                    loading = true;
//                }
//            }
//        });
    }

    private void loadmoreNEWS() {
        String WeiXinUrl = "http://route.showapi.com/582-2?" +
                "showapi_appid=42977" +
                "&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe" +
                "&page=" + page;
        Log.d("wode", "loadmoreNEWS: " + page);
        HttpUnitily.sendOkHttpRequest(WeiXinUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取更多信息失败", Toast.LENGTH_SHORT).show();
                        springView.onFinishFreshAndLoad();
                        swip.setEnabled(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String Weixinresponse = response.body().string();
                final WeiXinNew weiXinNew2 = Utility.handelWeiXinResponse(Weixinresponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weiXinNew2.showapi_res_code == 0) {
                            weiXinNew.showapi_res_body.pagebean.contentlist
                                    .addAll(weiXinNew2.showapi_res_body.pagebean.contentlist);
                            adapter.notifyDataSetChanged();
                            Log.d("wode", "run: notifyDataSetChanged");
                            springView.onFinishFreshAndLoad();
                            swip.setEnabled(true);
                        } else {
                            Toast.makeText(getActivity(), "获取更多信息失败", Toast.LENGTH_SHORT).show();
                            springView.onFinishFreshAndLoad();
                            swip.setEnabled(true);
                        }
                    }
                });
            }
        });
    }

    private void requestWexinNEWS() {
        String WeiXinUrl = "http://route.showapi.com/582-2?" +
                "showapi_appid=42977" +
                "&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe" +
                "&page=1";
        HttpUnitily.sendOkHttpRequest(WeiXinUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                        networkerro.setVisibility(View.VISIBLE);
                        bookrecyclerView.setVisibility(View.GONE);
                        swip.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String Weixinresponse = response.body().string();
                final WeiXinNew weiXinNew1 = Utility.handelWeiXinResponse(Weixinresponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weiXinNew1.showapi_res_code == 0) {
                            returnWeixin(weiXinNew1);
                            showBook();
                            bookrecyclerView.setVisibility(View.VISIBLE);
                            networkerro.setVisibility(View.GONE);
                            page = 1;
                            swip.setRefreshing(false);
                        } else {
                            Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                            swip.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    private void returnWeixin(WeiXinNew weiXinNew1) {
        this.weiXinNew = weiXinNew1;
    }

    private void showBook() {
        adapter = new Book_rc_Adapter(weiXinNew.showapi_res_body.pagebean.contentlist);
        bookrecyclerView.setAdapter(adapter);
    }
//第一次请求加载数据dialog
//    private void CloseProgressDialog() {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//    }
//
//    private void showProgressDialog() {
//        if (dialog == null) {
//            dialog = new CustomDialog(getActivity(), R.style.CustomDialog);
//            dialog.show();
//        }
//        dialog.show();
//    }

}
