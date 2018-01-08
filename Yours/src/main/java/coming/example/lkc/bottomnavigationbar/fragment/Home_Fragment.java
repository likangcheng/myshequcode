package coming.example.lkc.bottomnavigationbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.HomeCardActivity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Home_rc_newAdapter;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Home_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private Home_rc_newAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int newspage = 0;
    private List<JiSuApi_List> api_lists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, null);
        recyclerView = view.findViewById(R.id.home_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.home_swiplayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.bule, R.color.orange, R.color.teal);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        newsAdapter = new Home_rc_newAdapter(view.getContext());
        //点击事件
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JiSuApi_List jiSuApi_list=api_lists.get(position);
                Intent intent = new Intent(view.getContext(), HomeCardActivity.class);
                intent.putExtra(HomeCardActivity.CONTENTLIST_DATA, jiSuApi_list);
                view.getContext().startActivity(intent);
            }
        });
        //recyclerView的过度动画
//        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        newsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (newspage < 6) {
                    newspage++;
                    loadmoreNews(20 * newspage);
                } else {
                    newsAdapter.loadMoreEnd();
                }
            }
        }, recyclerView);
        recyclerView.setAdapter(newsAdapter);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                newspage = 1;
                requestNews();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                api_lists.clear();
                newspage = 1;
                requestNews();
            }
        });
    }


    private void requestNews() {
        newsAdapter.setEnableLoadMore(false);
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=头条&start=0&num=20&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            newsAdapter.setEnableLoadMore(true);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final JiSuApi_Body jiSuApi_body = Utility.handelNewsResponse(newsResponse);
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiSuApi_body != null) {
                                if (jiSuApi_body.status.equals("0")) {
                                    api_lists.addAll(jiSuApi_body.result.Newslist);
                                    newsAdapter.setNewData(jiSuApi_body.result.Newslist);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                            }
                            newsAdapter.setEnableLoadMore(true);
                            swipeRefreshLayout.setRefreshing(false);
//                            OnRefrsh_Success_Dialog();
                        }
                    });
                }
            }
        });
    }

    private void loadmoreNews(int start) {
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=新闻&start=" + start + "&num=20&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            newsAdapter.loadMoreFail();
                            newspage--;
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final JiSuApi_Body jiSuApi_body = Utility.handelNewsResponse(newsResponse);
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiSuApi_body != null) {
                                if (jiSuApi_body.status.equals("0")) {
                                    api_lists.addAll(jiSuApi_body.result.Newslist);
                                    newsAdapter.addData(jiSuApi_body.result.Newslist);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                            newsAdapter.loadMoreComplete();
                        }
                    });
                }
            }
        });
    }
//    private void OnRefrsh_Success_Dialog(){
//        Dialog dialog=new Dialog(getActivity(),R.style.OnRefrwsh_Suceess_DialogStyle);
//        View view=LayoutInflater.from(getActivity()).inflate(R.layout.refresh_success_layout,null);
//        dialog.setContentView(view);
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.TOP);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        int y[]=new int[2];
//        li.getLocationInWindow(y);
//        Log.d("test2", "OnRefrsh_Success_Dialog: "+y[1]);
//        lp.y=100;
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//
//    }
}
