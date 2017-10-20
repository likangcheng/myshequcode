package coming.example.lkc.bottomnavigationbar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Home_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Home_Fragment extends Fragment {
    private XRecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private Home_rc_Adapter newsAdapter;
    private int newspage = 1;
    private LinearLayout li;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, null);
        recyclerView = (XRecyclerView) view.findViewById(R.id.home_recyclerView);
        li= (LinearLayout) view.findViewById(R.id.home_linearlayout);
        layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setArrowImageView(R.drawable.ondown);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        newsAdapter = new Home_rc_Adapter();
        recyclerView.setAdapter(newsAdapter);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requestNews();
                newspage = 1;
            }

            @Override
            public void onLoadMore() {
                if (newspage < 6) {
                    loadmoreNews(20 * newspage);
                    newspage++;
                } else {
                    recyclerView.setNoMore(true);
                }
            }
        });
        recyclerView.refresh();
    }


    private void requestNews() {
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=头条&start=0&num=20&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            recyclerView.refreshComplete();
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
                                    newsAdapter.setDatalist(jiSuApi_body.result.Newslist);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                            }
                            recyclerView.refreshComplete();
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
                            recyclerView.loadMoreComplete();
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
                                    newsAdapter.loadmoreDatalist(jiSuApi_body.result.Newslist);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                            recyclerView.loadMoreComplete();
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
