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
import android.widget.Toast;

import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Game_rc_Apapter;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import coming.example.lkc.bottomnavigationbar.viewholder.Custom_Header_VH;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Movie_Fragment extends Fragment {
    private RecyclerView movie_recyclerview;
    private SwipeRefreshLayout swip;
    private Game_rc_Apapter adapter;
    private String picurl = "https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/picture";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie, container, false);
        movie_recyclerview = (RecyclerView) view.findViewById(R.id.movie_rcview);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.swip_movie);
        swip.setColorSchemeResources(R.color.colorAccent, R.color.bule, R.color.orange, R.color.teal);
        showAdapter();
        return view;
    }

    private void showAdapter() {
        adapter = new Game_rc_Apapter(getActivity());
        movie_recyclerview.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        movie_recyclerview.setLayoutManager(layoutManager);
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
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=娱乐&start=0&num=40&appkey=9a46b272586356ee";
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
                                if (jiSuApi_body.status.equals("0")) {
                                    adapter.GameAdapterSetData(jiSuApi_body.result.Newslist);
                                    initPic2Json();
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                            }
                            swip.setRefreshing(false);
                        }
                    });
                }
            }
        });

    }

    private void initPic2Json() {
        Log.d("wode", "initPic2Json: ");
        HttpUnitily.sendOkHttpRequest(picurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "广告数据异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JsonString = response.body().string();
                final List<String> picstring = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(JsonString);
                    picstring.add(jsonObject.getString("pic_1"));
                    picstring.add(jsonObject.getString("pic_2"));
                    picstring.add(jsonObject.getString("pic_3"));
                    picstring.add(jsonObject.getString("pic_4"));
                    picstring.add(jsonObject.getString("pic_5"));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Custom_Header_VH.adapter.setDate(picstring);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
