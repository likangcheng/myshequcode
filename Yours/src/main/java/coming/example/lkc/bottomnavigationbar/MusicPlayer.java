package coming.example.lkc.bottomnavigationbar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.listener.MusicPlayOrPause;
import coming.example.lkc.bottomnavigationbar.service.MusicService;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by lkc on 2017/10/11.
 */

public class MusicPlayer extends AppCompatActivity implements View.OnClickListener {
    private ImageView mp_backimg;
    private SingList sing;
    private CircleImageView mp_icon;
    private ObjectAnimator mp_icon_obanimator;
    private TextView time_left, time_right;
    private ImageView mp_play, mp_l_next, mp_r_next, mp_loop, mp_list;
    private MusicService.MusicBinder musicBinder;
    private DiscreteSeekBar seekBar;
    private final static String APP_ID = "wxd6ab7c22e73907b9";//微信ID
    private IWXAPI iwxapi;

    private void regtoWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }

    //用ServiceConnection绑定一个服务
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            musicBinder.initMusicBinder(listener);//绑定监听器
            musicBinder.nextMusic(sing);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initmp_icon_Animation(float mp_iconRotation) {
        mp_icon_obanimator = ObjectAnimator.ofFloat(mp_icon, "Rotation", mp_iconRotation, 360f);
        mp_icon_obanimator.setDuration(36000);
        mp_icon_obanimator.setRepeatMode(ValueAnimator.RESTART);
        mp_icon_obanimator.setRepeatCount(10);
        mp_icon_obanimator.setInterpolator(new LinearInterpolator());
    }

    //播放状态监听回调
    private MusicPlayOrPause listener = new MusicPlayOrPause() {
        @Override
        public void Play() {
            mp_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            initmp_icon_Animation(mp_icon.getRotation());//播放器中间图标滚动动画
            mp_icon_obanimator.start();
        }

        @Override
        public void Pause() {
            mp_icon_obanimator.cancel();
            mp_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
        }

        @Override
        public void isLooping(boolean looping) {
            if (looping) {
                Toast.makeText(MusicPlayer.this, "单曲循环", Toast.LENGTH_SHORT).show();
                mp_loop.setImageDrawable(getResources().getDrawable(R.drawable.order));
            } else {
                Toast.makeText(MusicPlayer.this, "顺序播放", Toast.LENGTH_SHORT).show();
                mp_loop.setImageDrawable(getResources().getDrawable(R.drawable.loop));
            }
        }

        @Override
        public void AutoNext() {

        }

        @Override
        public void Progress(int progress, int lefttext, int righttext) {
            //监听播放条状态
            seekBar.setProgress(progress);
            time_left.setText(new SimpleDateFormat("mm:ss").format(lefttext));
            time_right.setText(new SimpleDateFormat("mm:ss").format(righttext));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDK>21的用户全屏显示
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.music_player_layout);
        initFrist();//准备工作初始化
        initToolbar();
        initmp_backimg();
        initseekBar();
    }

    private void initseekBar() {
        //滚动条时间监听
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                //通过服务返回播放位置
                int current = musicBinder.onProgressChanged(value);
                if (current != 0) {
                    time_left.setText(new SimpleDateFormat("mm:ss").format(current));
                }

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                musicBinder.onStartTrackingTouch();//开始滑动滚动条通知服务
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                musicBinder.onStopTrackingTouch(seekBar.getProgress());//结束滑动滚动条通知服务
            }
        });

    }

    private void initFrist() {
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        regtoWX(this);//验证微信ID
        sing = (SingList) getIntent().getSerializableExtra("MUSIC_DATA");
        seekBar = (DiscreteSeekBar) findViewById(R.id.seekbar_1);
        time_left = (TextView) findViewById(R.id.time_left);
        time_right = (TextView) findViewById(R.id.time_right);
        mp_play = (ImageView) findViewById(R.id.mp_play);
        mp_play.setOnClickListener(this);
        mp_loop = (ImageView) findViewById(R.id.mp_loop);
        mp_loop.setOnClickListener(this);
        mp_l_next = (ImageView) findViewById(R.id.mp_left_next);
        mp_l_next.setOnClickListener(this);
        mp_r_next = (ImageView) findViewById(R.id.mp_right_next);
        mp_r_next.setOnClickListener(this);
        mp_list = (ImageView) findViewById(R.id.mp_list);
        mp_list.setOnClickListener(this);
    }

    //播放器背景
    private void initmp_backimg() {
        mp_backimg = (ImageView) findViewById(R.id.music_player_backimg);
        mp_icon = (CircleImageView) findViewById(R.id.music_player_icon);
        Glide.with(this).load(sing.albumpic_big).bitmapTransform(new BlurTransformation(this, 25),
                new CenterCrop(this)).into(mp_backimg);
        Glide.with(this).load(sing.albumpic_big).into(mp_icon);
    }

    //toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.music_player_toolbar);
        toolbar.setTitle(sing.songname);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mp_play:
                musicBinder.startMusic(this);
                break;
            case R.id.mp_left_next:
                break;
            case R.id.mp_right_next:
                break;
            case R.id.mp_loop:
                musicBinder.setloop();
                break;
            case R.id.mp_list:
                break;
            default:
                throw new NullPointerException();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mp_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.mp_share:
                initshare();
                break;
            default:
                throw new NullPointerException();
        }
        return true;
    }

    private void initshare() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_yours);
        builder.setTitle("分享到");
        String[] items = {"微信好友", "朋友圈"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                share2WeiXin(which);
            }
        });
        builder.create();
        builder.show();
    }

    private void share2WeiXin(final int which) {
        Glide.with(this).load(sing.albumpic_small).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                WXMusicObject wxmusic = new WXMusicObject();
                wxmusic.musicUrl = sing.musicurl;
                WXMediaMessage mediaMessage = new WXMediaMessage();
                mediaMessage.mediaObject = wxmusic;
                mediaMessage.title = sing.songname;
                mediaMessage.description = sing.singername;
                mediaMessage.thumbData = Bitmap2Bytes(resource);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = mediaMessage;
                if (which == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else if (which == 1) {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                iwxapi.sendReq(req);
            }
        });
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        stopService(new Intent(this, MusicService.class));
    }
}
