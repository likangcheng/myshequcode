package coming.example.lkc.bottomnavigationbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.adapter.Collection_Song_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.UserSong_Collection;
import coming.example.lkc.bottomnavigationbar.listener.SongCount_Listener;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;

public class CollectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Collection_Song_Adapter adapter;
    private TextView user_tv, songsize_tv;
    private SongCount_Listener listener = new SongCount_Listener() {
        @Override
        public void getCount(int count) {
            songsize_tv.setText("当前收藏" + count + "首歌");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        recyclerView = (RecyclerView) findViewById(R.id.song_collection_rcview);
        user_tv = (TextView) findViewById(R.id.collection_user);
        songsize_tv = (TextView) findViewById(R.id.collection_songsize);
        boolean flag = SharedPreferencesUnitl.getLoginstatus_SharedPreferencesEditor(this);
        if (flag) {
            String username = SharedPreferencesUnitl.getUsername_SharedPreferencesEditor(this);
            user_tv.setText(username + "的收藏");
            List<UserSong_Collection> datalist = DataSupport.where("username = ?", username).
                    order("collection_date desc").find(UserSong_Collection.class);
            songsize_tv.setText("当前收藏" + datalist.size() + "首歌");
            if (datalist.size() == 0) {
                Toast.makeText(this, "你还没有收藏歌曲", Toast.LENGTH_SHORT).show();
            }
            adapter = new Collection_Song_Adapter(datalist, this, listener);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

}
