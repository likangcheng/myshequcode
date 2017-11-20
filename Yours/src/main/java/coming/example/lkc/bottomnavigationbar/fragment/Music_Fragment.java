package coming.example.lkc.bottomnavigationbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.MusicPlayer;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Music_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Music_Fragment extends Fragment {
    private RecyclerView musicrecyclerView;
    private GridLayoutManager gridLayoutManager;
    private Music_rc_Adapter madapter;
    private CustomDialog dialog;//显示加载的对话框
    private List<SingList> singlist;//音乐资源
    private static int lastposition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music, null);
        musicrecyclerView = (RecyclerView) view.findViewById(R.id.music_recyclerview);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        musicrecyclerView.setLayoutManager(gridLayoutManager);
        madapter = new Music_rc_Adapter();
        musicrecyclerView.setAdapter(madapter);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressDialog();//加载对话框
        querySingList();//查询可取列表
    }

    private void querySingList() {
        String singUrl = "http://route.showapi.com/213-4?showapi_appid=42977" +
                "&showapi_sign=5e9e2850cf574e4fbb358230ff31fafe&topid=26";
        HttpUnitily.sendOkHttpRequest(singUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CloseProgressDialog();
                        Toast.makeText(getActivity(), "网络出现问题", Toast.LENGTH_SHORT).show();
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
                                Music_Fragment.this.singlist = music.showapi_res_body.pagebean.songlist;
                                madapter.setMusicData(music.showapi_res_body.pagebean.songlist);
                            } else {
                                Toast.makeText(getActivity(), "数据出现问题", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                        }
                        CloseProgressDialog();
                    }
                });
            }
        });
        madapter.setOnItemClickListener(new Music_rc_Adapter.OnclickMusicData() {
            @Override
            public void MusicData(int Position) {
                click2musicplayer(Position);
            }
        });
    }

    private void click2musicplayer(int position) {
        if (lastposition == -1 || lastposition == position) {
            lastposition = position;
            Intent intent = new Intent(getActivity(), MusicPlayer.class);
            intent.putExtra("MUSIC_DATA", (Serializable) singlist);
            intent.putExtra("MUSIC_DATA_INT", position);
            intent.putExtra("FLAG", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getActivity().startActivity(intent);
        } else {
            lastposition = position;
            Intent broadcastintent = new Intent("coming.example.lkc.bottomnavigationbar." +
                    "FINISH_MUSICPLAYER");
            broadcastintent.putExtra("MUSIC_DATA_NEWPOSITION", position);
            getActivity().sendBroadcast(broadcastintent);
            Intent intent = new Intent(getActivity(), MusicPlayer.class);
            intent.putExtra("MUSIC_DATA", (Serializable) singlist);
            intent.putExtra("MUSIC_DATA_INT", position);
            intent.putExtra("FLAG", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getActivity().startActivity(intent);

        }
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
