package coming.example.lkc.bottomnavigationbar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swip;
    private GridLayoutManager layoutManager;
    private JiSuApi_Body jiSuApi_body;
    private Home_rc_Adapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.swip_home_layout);
        swip.setColorSchemeResources(R.color.colorAccent);
        layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new Home_rc_Adapter();
        recyclerView.setAdapter(newsAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
                requestNews();
            }
        });

        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNews();
            }
        });
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
                            swip.setRefreshing(false);
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
                                if (jiSuApi_body.status == 0) {
                                   newsAdapter.setDatalist(jiSuApi_body.result.Newslist);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                            swip.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

}
