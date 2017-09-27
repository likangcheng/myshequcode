package coming.example.lkc.bottomnavigationbar.fragment;

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
public class Music_Fragment extends Fragment implements View.OnClickListener {
    private RecyclerView musicrecyclerView;
    private GridLayoutManager gridLayoutManager;
    private Music_rc_Adapter madapter;
    private final static String APP_ID = "wxd6ab7c22e73907b9";
    private IWXAPI iwxapi;

    private void regtoWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }

    private CustomDialog dialog;
    private ImageView music_next, music_go;
    private TextView sing_name, singer, time_left, time_right;
    private DiscreteSeekBar seekBar;
    private CircleImageView music_icon;
    private List<SingList> singlist;
    private int position;
    private MusicService.MusicBinder musicBinder;
    private int poisition_copy = 0, Next_Music_Code = 1, First_AUTONEXT = 0;
    private MusicPlayOrPause listener = new MusicPlayOrPause() {
        @Override
        public void Play() {
            music_go.setImageResource(R.drawable.pause);
        }

        @Override
        public void Pause() {
            music_go.setImageResource(R.drawable.play);
        }

        @Override
        public void AutoNext() {
            Next();
        }

        @Override
        public void Progress(int progress, int lefttext, int righttext) {
            seekBar.setProgress(progress);
            time_left.setText(new SimpleDateFormat("mm:ss").format(lefttext));
            time_right.setText(new SimpleDateFormat("mm:ss").format(righttext));

        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music, null);
        musicrecyclerView = (RecyclerView) view.findViewById(R.id.music_recyclerview);
        init(view);
        regtoWX(view.getContext());
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        musicrecyclerView.setLayoutManager(gridLayoutManager);
        madapter = new Music_rc_Adapter();
        musicrecyclerView.setAdapter(madapter);
        return view;
    }

    private void init(View view) {
        music_icon = (CircleImageView) view.findViewById(R.id.music_icon);
        music_next = (ImageView) view.findViewById(R.id.music_next);
        music_go = (ImageView) view.findViewById(R.id.music_go);
        sing_name = (TextView) view.findViewById(R.id.sing_name);
        singer = (TextView) view.findViewById(R.id.singer);
        seekBar = (DiscreteSeekBar) view.findViewById(R.id.seekbar_1);
        time_left = (TextView) view.findViewById(R.id.time_left);
        time_right = (TextView) view.findViewById(R.id.time_right);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        seekBar.setVisibility(View.GONE);
        showProgressDialog();
        querySingList();
        music_go.setOnClickListener(this);
        music_next.setOnClickListener(this);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                int current = musicBinder.onProgressChanged(value);
                if (current != 0) {
                    time_left.setText(new SimpleDateFormat("mm:ss").format(current));
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                musicBinder.onStartTrackingTouch();
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                musicBinder.onStopTrackingTouch(seekBar.getProgress());
            }
        });
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
                Music_Fragment.this.singlist = music.showapi_res_body.pagebean.songlist;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (music.showapi_res_code == 0) {
                            madapter.setMusicData(music.showapi_res_body.pagebean.songlist);
                        } else {
                            Toast.makeText(getActivity(), "数据出现问题", Toast.LENGTH_SHORT).show();
                        }
                        CloseProgressDialog();
                    }
                });
            }
        });
        madapter.setOnLongItemClickListenter(new Music_rc_Adapter.OnLongClick() {
            @Override
            public void FengXiang(View v, int Position) {
                initFX(Position);
            }
        });
        madapter.setOnItemClickListener(new Music_rc_Adapter.OnclickMusicData() {
            @Override
            public void MusicData(int Position) {
                SingList sing = singlist.get(Position);
                Music_Fragment.this.position = Position;
                Glide.with(getActivity()).load(sing.albumpic_small).into(music_icon);
                sing_name.setText(sing.songname);
                singer.setText(sing.singername);
                NextMusic_Select();
                if (First_AUTONEXT == 0) {
                    musicBinder.initMusicBinder(listener);
                    seekBar.setVisibility(View.VISIBLE);
                    musicBinder.initMusicPlayAutoNext();
                    First_AUTONEXT = 1;
                }
            }
        });
    }

    private void initFX(final int position) {
        Glide.with(getActivity()).load(singlist.get(position).albumpic_small).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                WXMusicObject wxmusic = new WXMusicObject();
                wxmusic.musicUrl = singlist.get(position).musicurl;
                WXMediaMessage mediaMessage = new WXMediaMessage();
                mediaMessage.mediaObject = wxmusic;
                mediaMessage.title = singlist.get(position).songname;
                mediaMessage.description = singlist.get(position).singername;
                mediaMessage.thumbData = Bitmap2Bytes(resource);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = mediaMessage;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                iwxapi.sendReq(req);
            }
        });
    }

    private void Next() {
        musicBinder.nextMusic(singlist.get(position + Next_Music_Code));
        Glide.with(getActivity()).load(singlist.get(position + Next_Music_Code).albumpic_small).into(music_icon);
        sing_name.setText(singlist.get(position + Next_Music_Code).songname);
        singer.setText(singlist.get(position + Next_Music_Code).singername);
        position = position + Next_Music_Code;
        poisition_copy = position;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(connection);
        getActivity().stopService(new Intent(getActivity(), MusicService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_go:
                musicBinder.startMusic(getActivity());
                break;
            case R.id.music_next:
                musicBinder.nextMusic(singlist.get(position + Next_Music_Code));
                Next();
                break;
            default:
                break;
        }
    }

    private void NextMusic_Select() {
        if (poisition_copy != position || position == 0) {
            musicBinder.nextMusic(singlist.get(position));
            poisition_copy = position;
        } else {
            musicBinder.startMusic(getActivity());
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

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
