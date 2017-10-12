package coming.example.lkc.bottomnavigationbar.fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.MusicPlayer;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Music_rc_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.listener.MusicPlayOrPause;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.service.MusicService;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Music_Fragment extends Fragment  {
    private RecyclerView musicrecyclerView;
    private GridLayoutManager gridLayoutManager;
    private Music_rc_Adapter madapter;
    private CustomDialog dialog;//显示加载的对话框
    private List<SingList> singlist;//音乐资源

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
                        if (music != null){
                            if (music.showapi_res_code == 0) {
                                Music_Fragment.this.singlist = music.showapi_res_body.pagebean.songlist;
                                madapter.setMusicData(music.showapi_res_body.pagebean.songlist);
                            } else {
                                Toast.makeText(getActivity(), "数据出现问题", Toast.LENGTH_SHORT).show();
                            }
                        }else {
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
                SingList sing = singlist.get(Position);
                Intent intent=new Intent(getActivity(), MusicPlayer.class);
                intent.putExtra("MUSIC_DATA",sing);
                getActivity().startActivity(intent);
//                Music_Fragment.this.position = Position;
//                Glide.with(getActivity()).load(sing.albumpic_small).into(music_icon);
//                sing_name.setText(sing.songname);
//                singer.setText(sing.singername);
//                NextMusic_Select();
//                if (First_AUTONEXT == 0) {
//                    initPlayer();
//                    musicBinder.initMusicBinder(listener);
//                    musicBinder.initMusicPlayAutoNext();
//                    First_AUTONEXT = 1;
//                }
            }
        });
    }



//    private void Next() {
//        //下一首
//        musicBinder.nextMusic(singlist.get(position + Next_Music_Code));
//        Glide.with(getActivity()).load(singlist.get(position + Next_Music_Code).albumpic_small).into(music_icon);
//        sing_name.setText(singlist.get(position + Next_Music_Code).songname);
//        singer.setText(singlist.get(position + Next_Music_Code).singername);
//        position = position + Next_Music_Code;
//        poisition_copy = position;
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.music_go:
//                musicBinder.startMusic(getActivity());
//                break;
//            case R.id.music_next:
//                musicBinder.nextMusic(singlist.get(position + Next_Music_Code));
//                Next();
//                break;
//            default:
//                break;
//        }
//    }

//    private void NextMusic_Select() {
//        //当前播放曲目序列号如果为初始化值，则直接跳position
//        if (poisition_copy == -1) {
//            musicBinder.nextMusic(singlist.get(position));
//            poisition_copy = position;
//        } else if (poisition_copy != position) {
//            //不想等则直接next
//            musicBinder.nextMusic(singlist.get(position));
//            poisition_copy = position;
//        } else {
//            //相等则暂停
//            musicBinder.startMusic(getActivity());
//        }
//    }

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
