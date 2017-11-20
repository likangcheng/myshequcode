package coming.example.lkc.bottomnavigationbar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.adapter.Collection_Song_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.dao.UserSong_Collection;
import coming.example.lkc.bottomnavigationbar.unitl.ActivityCollector;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;

public class CollectionActivity extends MyBaseActivity {
    private RecyclerView recyclerView;
    private Collection_Song_Adapter adapter;
    private TextView user_tv, songsize_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        recyclerView = (RecyclerView) findViewById(R.id.song_collection_rcview);
        user_tv = (TextView) findViewById(R.id.collection_user);
        songsize_tv = (TextView) findViewById(R.id.collection_songsize);
        boolean flag = SharedPreferencesUnitl.getLoginstatus_SharedPreferencesEditor(this);
        if (flag) {
            String username = SharedPreferencesUnitl.getUsername_SharedPreferencesEditor(this);
            user_tv.setText(username + "的收藏");
            final List<UserSong_Collection> datalist = DataSupport.where("username = ?", username).
                    order("collection_date desc").find(UserSong_Collection.class);
            songsize_tv.setText("当前收藏" + datalist.size() + "首歌");
            if (datalist.size() == 0) {
                Toast.makeText(this, "你还没有收藏歌曲", Toast.LENGTH_SHORT).show();
            }
            adapter = new Collection_Song_Adapter(datalist, this);
            adapter.setgetCountonChangeListener(new Collection_Song_Adapter.Songcount_listener() {
                @Override
                public void getCount(int position) {
                    songsize_tv.setText("当前收藏" + position + "首歌");
                }
            });
            adapter.setOnPositionClikcListener(new Collection_Song_Adapter.OnClicklistener() {
                @Override
                public void OnClick(View v, int position) {
                    Activity activity = ActivityCollector.queryActivity("MusicPlayer");
                    if (activity != null) {
                        if (!activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                    Intent intent = new Intent(CollectionActivity.this, MusicPlayer.class);
                    List<SingList> singLists = collection2singlist(datalist);
                    intent.putExtra("MUSIC_DATA", (Serializable) singLists);
                    intent.putExtra("MUSIC_DATA_INT", position);
                    intent.putExtra("FLAG", 2);
                    startActivity(intent);
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setAdapter(adapter);

        }
    }

    private List<SingList> collection2singlist(List<UserSong_Collection> datalsit) {
        List<SingList> singLists = new ArrayList<>();
        for (UserSong_Collection us_c : datalsit) {
            SingList singList = new SingList();
            singList.songname = us_c.getSongname();
            singList.singername = us_c.getSinger();
            singList.albumpic_big = us_c.getBigpic();
            singList.albumpic_small = us_c.getSmallpic();
            singList.musicurl = us_c.getM4aurl();
            singList.m4a = us_c.getM4aurl();
            singLists.add(singList);
        }
        return singLists;
    }
}
