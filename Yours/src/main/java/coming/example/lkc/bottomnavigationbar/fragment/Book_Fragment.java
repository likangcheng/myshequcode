package coming.example.lkc.bottomnavigationbar.fragment;

import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.Book_Card_Activity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Book_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;
import coming.example.lkc.bottomnavigationbar.dao.WeiXin_Content_list;
import coming.example.lkc.bottomnavigationbar.other_view.GridSpacingItemDecoration;
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
    private Book_rc_Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout networkerro;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book, null);
        bookrecyclerView = view.findViewById(R.id.weixin_rc);
        networkerro = view.findViewById(R.id.book_network_erro);
        swipeRefreshLayout = view.findViewById(R.id.book_swiprefreshlayou);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.bule, R.color.orange, R.color.teal);
        bookrecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new Book_rc_Adapter(view.getContext());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseadapter, View view, int position) {
                Intent intent = new Intent(view.getContext(), Book_Card_Activity.class);
                intent.putExtra(Book_Card_Activity.WEIXIN_DATA, adapter.getData().get(position));
                view.getContext().startActivity(intent);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.d("wode", "onLoadMoreRequested: " + page);
                page++;
                loadmoreNEWS();
            }
        }, bookrecyclerView);
        bookrecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.recycleview_dimen), true));
        bookrecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        networkerro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                requestWexinNEWS();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                requestWexinNEWS();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                requestWexinNEWS();
            }
        });
    }

    private void loadmoreNEWS() {
        String WeiXinUrl = "http://route.showapi.com/582-2?" +
                "showapi_appid=42977" +
                "&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe" +
                "&page=" + page;
        HttpUnitily.sendOkHttpRequest(WeiXinUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取更多信息失败", Toast.LENGTH_SHORT).show();
                        adapter.loadMoreFail();
                        page--;
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
                        if (weiXinNew2 != null) {
                            if (weiXinNew2.showapi_res_code == 0) {
                                weiXinNew2.showapi_res_body.pagebean.contentlist.remove(0);
                                adapter.addData(weiXinNew2.showapi_res_body.pagebean.contentlist);
                                adapter.loadMoreComplete();
                            } else {
                                Toast.makeText(getActivity(), "获取更多信息失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
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
                        adapter.setEnableLoadMore(true);
                        swipeRefreshLayout.setRefreshing(false);
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
                        if (weiXinNew1 != null) {
                            if (weiXinNew1.showapi_res_code == 0) {
                                adapter.setNewData(weiXinNew1.showapi_res_body.pagebean.contentlist);
                                page = 1;
                            } else {
                                Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                        }
                        bookrecyclerView.setVisibility(View.VISIBLE);
                        networkerro.setVisibility(View.GONE);
                        adapter.setEnableLoadMore(true);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

}
