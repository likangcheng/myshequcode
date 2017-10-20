package coming.example.lkc.bottomnavigationbar.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.fragment.Music_Fragment;
import coming.example.lkc.bottomnavigationbar.listener.MusicPlayOrPause;

public class MusicService extends Service {
    private static MediaPlayer mediaPlayer;
    private static int count = 0;//1为暂停,0为播放
    private boolean PLAYSTAUCT_PAUSE;
    private MusicBinder mBinder = new MusicBinder();
    private boolean MUSIC_PREPARE_SUCUTS = false;//判断是否加载完成

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wode", "MusicService onCreate: ");
    }

    @Override
    public void onDestroy() {
        count = 1;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();//释放音乐文件资源
            mediaPlayer = null;
        }
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("wode", "MusicService onBind: ");
        return mBinder;
    }

    public class MusicBinder extends Binder {
        private MusicPlayOrPause listener;

        public void initMusicBinder(MusicPlayOrPause listener) {
            this.listener = listener;
        }

        public void startMusic(Context context) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    count = 1;
                    listener.Pause();
                } else {
                    if (MUSIC_PREPARE_SUCUTS) {
                        mediaPlayer.start();
                        count = 0;
                        listener.Play();
                        UpdateProgress();
                    } else {
                        Toast.makeText(MusicService.this, "音乐资源错误", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                new NullPointerException();
            }
        }

        public boolean setloop() {
            if (mediaPlayer != null) {
                if (mediaPlayer.isLooping()) {
                    mediaPlayer.setLooping(false);
                    listener.isLooping(false);
                    return false;
                } else {
                    mediaPlayer.setLooping(true);
                    listener.isLooping(true);
                    return true;
                }

            }
            return false;
        }

        public void nextMusic(SingList singlist) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            if (mediaPlayer != null) {
                //next
                if (mediaPlayer.isPlaying()) {
                    count = 1;
                    mediaPlayer.stop();
                    listener.Pause();
                }
                count = 1;
                mediaPlayer.reset();
                try {
                    if (!TextUtils.isEmpty(singlist.musicurl)) {
                        mediaPlayer.setDataSource(singlist.musicurl);
                    } else {
                        mediaPlayer.setDataSource(singlist.m4a);
                    }
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepareAsync();//异步加载
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Toast.makeText(MusicService.this, "音频资源出现错误", Toast.LENGTH_SHORT).show();
                            listener.OnError();
                            return false;
                        }
                    });
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            MUSIC_PREPARE_SUCUTS = true;
                            listener.isLooping(false);
                            mp.start();
                            count = 0;
                            UpdateProgress();
                            listener.SwitchBackground();
                            listener.Play();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public int onProgressChanged(int progress) {
            if (mediaPlayer != null) {
                int current = (int) (progress * mediaPlayer.getDuration() / 100.0);
                return current;
            } else {
                return 0;
            }
        }

        public void onStartTrackingTouch() {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    count = 1;
                    mediaPlayer.pause();
                    PLAYSTAUCT_PAUSE = true;
                } else {
                    PLAYSTAUCT_PAUSE = false;
                }
            }
        }

        public void onStopTrackingTouch(int progress) {
            if (mediaPlayer != null) {
                int current = (int) (progress * MusicService.mediaPlayer.getDuration() / 100.0);
                mediaPlayer.seekTo(current);
                if (PLAYSTAUCT_PAUSE) {
                    count = 0;
                    mediaPlayer.start();
                    UpdateProgress();
                } else {
                    mediaPlayer.pause();
                }

            }
        }

        public void initMusicPlayAutoNext() {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (count == 0) {
                        if (!mp.isLooping()) {
                            listener.AutoNext();
                        }
                    }
                }
            });
        }
    }


    private void UpdateProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count % 2 == 0) {
                    Message msg_ = Message.obtain();
                    int progress = (int) ((mediaPlayer.getCurrentPosition() * 100.0) / mediaPlayer.getDuration());
                    msg_.what = mediaPlayer.getCurrentPosition();  //获取当前播放位置
                    msg_.arg1 = progress;
                    msg_.arg2 = mediaPlayer.getDuration();
                    handler.sendMessage(msg_);
                    SystemClock.sleep(1000);    //延时一秒钟
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = msg.arg1;
            int current = msg.what;
            int current1 = msg.arg2;
            mBinder.listener.Progress(progress, current, current1);
        }
    };


}

