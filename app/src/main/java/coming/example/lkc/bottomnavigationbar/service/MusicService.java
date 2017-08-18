package coming.example.lkc.bottomnavigationbar.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.fragment.Music_Fragment;

public class MusicService extends Service {
    public static MediaPlayer mediaPlayer;
    public static int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
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

    private MusicBinder mBinder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MusicBinder extends Binder {
        public void startMusic(Context context) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    count = 1;
                    Music_Fragment.music_go.setImageResource(R.drawable.go);
                } else {
                    mediaPlayer.start();
                    count = 0;
                    Music_Fragment.music_go.setImageResource(R.drawable.paruse);
                    UpdateProgress();
                }

            } else {
                Toast.makeText(context, "请选择一首歌", Toast.LENGTH_SHORT).show();
            }
        }


        public void nextMusic(SingList singlist) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    Music_Fragment.music_go.setImageResource(R.drawable.go);
                }
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(singlist.musicurl);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            count = 0;
                            UpdateProgress();
                            Log.d("wode", "第二次next: ");
                            Music_Fragment.music_go.setImageResource(R.drawable.paruse);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(singlist.musicurl);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            UpdateProgress();
                            Log.d("wode", "第一次next: ");
                            count = 0;
                            Music_Fragment.music_go.setImageResource(R.drawable.paruse);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        public void stopMusic() {
//            count = 1;
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//                Log.d("wode", "关闭MediaPalyer: ");
//                mediaPlayer.release();//释放音乐文件资源
//                mediaPlayer = null;
//            } else {
//                mediaPlayer.release();
//            }
//        }
    }

    public static void UpdateProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count % 2 == 0) {
                    Message msg_ = Message.obtain();
                    int progress = (int) ((mediaPlayer.getCurrentPosition() * 100.0) / mediaPlayer.getDuration());
                    msg_.what = mediaPlayer.getCurrentPosition();  //获取当前播放位置
                    msg_.arg1 = progress;
                    msg_.arg2 = mediaPlayer.getDuration();
                    Music_Fragment.handler.sendMessage(msg_);
                    SystemClock.sleep(1000);    //延时一秒钟
                }
            }
        }).start();

    }


}

