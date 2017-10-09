package coming.example.lkc.bottomnavigationbar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
    private SpringView springView;
    private Book_rc_Adapter adapter;
    private LinearLayout networkerro;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book, null);
        bookrecyclerView = (RecyclerView) view.findViewById(R.id.weixin_rc);
        networkerro = (LinearLayout) view.findViewById(R.id.book_network_erro);
        springView = (SpringView) view.findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //RecyclerView滑动过程中不断请求layout的Request，不断调整item见的间隙，并且是在item尺寸显示前预处理，因此解决RecyclerView滑动到顶部时仍会出现移动问题
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        bookrecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bookrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }
        });
        adapter = new Book_rc_Adapter();
        bookrecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestWexinNEWS();
        networkerro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWexinNEWS();
            }
        });
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                requestWexinNEWS();
            }

            @Override
            public void onLoadmore() {
                page++;
                loadmoreNEWS();
            }
        });

        springView.setHeader(new AliHeader(getActivity(), true));
        springView.setFooter(new AliFooter(getActivity(), false));
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
                        springView.onFinishFreshAndLoad();
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
                                adapter.loadmoreBookData(weiXinNew2.showapi_res_body.pagebean.contentlist);
                            } else {
                                Toast.makeText(getActivity(), "获取更多信息失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                        }
                        springView.onFinishFreshAndLoad();
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
                        springView.onFinishFreshAndLoad();
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
                                adapter.setBookData(weiXinNew1.showapi_res_body.pagebean.contentlist);
                                page = 1;
                            } else {
                                Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                        }
                        bookrecyclerView.setVisibility(View.VISIBLE);
                        networkerro.setVisibility(View.GONE);
                        springView.onFinishFreshAndLoad();
                    }
                });
            }
        });
    }

}
