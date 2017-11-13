package coming.example.lkc.bottomnavigationbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.MusicPlayer;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Music_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
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

public class Search_Music_Fragment extends Fragment {
    private RecyclerView music_recyclerview;
    private LinearLayout no_search;
    private Music_rc_Adapter adapter;
    private List<SingList> singlist;
    private CustomDialog dialog;
    private String search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_music_layout, null);
        music_recyclerview = (RecyclerView) view.findViewById(R.id.search_music_recyclerview);
        no_search = (LinearLayout) view.findViewById(R.id.no_music_search);
        adapter = new Music_rc_Adapter();
        music_recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        music_recyclerview.setAdapter(adapter);
        Log.d("wode", "onCreateView: music");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("test2", "onActivityCreated: music");
        adapter.setOnItemClickListener(new Music_rc_Adapter.OnclickMusicData() {
            @Override
            public void MusicData(int Position) {
                Intent intent = new Intent(getActivity(), MusicPlayer.class);
                intent.putExtra("MUSIC_DATA", (Serializable) singlist);
                intent.putExtra("MUSIC_DATA_INT", Position);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (search != null) {
                initSearch(search);
                showProgressDialog();
                search = null;
            }
        }
    }

    public void SearchString(String s) {
        search = s;
    }

    private void initSearch(String s) {
        String singUrl = "http://route.showapi.com/213-1?showapi_appid=42977" +
                "&keyword=" + s + "&page=1&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe";
        HttpUnitily.sendOkHttpRequest(singUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络出现问题", Toast.LENGTH_SHORT).show();
                        CloseProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String singResponse = response.body().string();
                final Music music = Utility.handelMusicResponse(singResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (music != null) {
                            if (music.showapi_res_code == 0) {
                                Search_Music_Fragment.this.singlist = music.showapi_res_body.pagebean.contentlist;
                                adapter.setMusicData(music.showapi_res_body.pagebean.contentlist);
                            } else {
                                Toast.makeText(getActivity(), "数据出现问题", Toast.LENGTH_SHORT).show();
                            }
                            if (music.showapi_res_body.pagebean.allPages == 0) {
                                Toast.makeText(getActivity(), "搜索的内容不存在", Toast.LENGTH_SHORT).show();
                                music_recyclerview.setVisibility(View.GONE);
                                no_search.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                        }
                        CloseProgressDialog();
                    }
                });
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
}
