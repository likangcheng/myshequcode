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

import java.io.IOException;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.SearchActivity;
import coming.example.lkc.bottomnavigationbar.adapter.Book_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.adapter.Game_rc_Apapter;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;
import coming.example.lkc.bottomnavigationbar.listener.Search2Fragment;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/10/16.
 */

public class Search_WeiXin_Fragment extends Fragment implements Search2Fragment {
    private Book_rc_Adapter search_weixin_adapter;
    private RecyclerView recyclerView;
    private CustomDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_weixin_layout, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_weixin_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        search_weixin_adapter = new Book_rc_Adapter();
        recyclerView.setAdapter(search_weixin_adapter);
        Log.d("test2", "onCreateView: WeixinFragment ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("test2", "onActivityCreated: weixin");

    }

    private void initSearch(String s) {
        String NewsUrl = "http://route.showapi.com/582-2?showapi_appid=42977&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe"
                + "&key=" + s;
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "网络状况异常", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                CloseProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final WeiXinNew weiXinNew = Utility.handelWeiXinResponse(newsResponse);
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weiXinNew != null) {
                                if (weiXinNew.showapi_res_body.pagebean.allNum != 0) {
                                    search_weixin_adapter.setBookData(weiXinNew.showapi_res_body.pagebean.contentlist);
                                    recyclerView.smoothScrollToPosition(0);
                                } else {
                                    Toast.makeText(getActivity(), "搜索的内容不存在", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //获取对象为空，一般是网络可以访问，但已被拦截，而且能够获取到JSON返回值，但是值乱码所以JSon序列化
                                //会失效。
                                Toast.makeText(getActivity(), "请检测网络是否连接异常", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }

                        }
                    });
                }
                CloseProgressDialog();
            }
        });

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

    @Override
    public void SearchString(String s) {
        Log.d("test2", "SearchString:weixin");
        showProgressDialog();
        initSearch(s);
    }
}
