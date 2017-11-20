package coming.example.lkc.bottomnavigationbar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Game_Fragment extends Fragment {
    private RecyclerView game_recyclerview;
    private Game_rc_Apapter adapter;
    private SpringView springView;
    private String picurl = "https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/picture";
    private CustomDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game, container, false);
        game_recyclerview = (RecyclerView) view.findViewById(R.id.game_rcview);
        springView = (SpringView) view.findViewById(R.id.game_springview);
        showAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressDialog();
        requestNews();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                requestNews();
            }

            @Override
            public void onLoadmore() {

            }
        });
        springView.setHeader(new MeituanHeader(getActivity()));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new MeituanFooter(getActivity()));

    }

    private void requestNews() {
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=NBA&start=0&num=40&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            CloseProgressDialog();
                            springView.onFinishFreshAndLoad();
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
                                    initPic2Json(jiSuApi_body);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initPic2Json(final JiSuApi_Body jiSuApi_body) {
        Log.d("wode", "initPic2Json: ");
        HttpUnitily.sendOkHttpRequest(picurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "广告数据异常", Toast.LENGTH_SHORT).show();
                        CloseProgressDialog();
                        springView.onFinishFreshAndLoad();
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
                            adapter.GameAdapterSetData(jiSuApi_body.result.Newslist, picstring);
                            CloseProgressDialog();
                            springView.onFinishFreshAndLoad();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAdapter() {
        adapter = new Game_rc_Apapter(getActivity());
        Log.d("wode", "showAdapter: ");
        game_recyclerview.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        game_recyclerview.setLayoutManager(layoutManager);
    }


    private void CloseProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new CustomDialog(getActivity(), R.style.CustomDialog);
            dialog.show();
        }
        dialog.show();
    }

}
