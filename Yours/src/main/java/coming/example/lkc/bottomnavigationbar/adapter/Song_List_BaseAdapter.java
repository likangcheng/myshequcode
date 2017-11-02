package coming.example.lkc.bottomnavigationbar.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.dao.UserSong_Collection;
import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;

/**
 * Created by lkc on 2017/10/13.
 */

public class Song_List_BaseAdapter extends BaseAdapter {
    private List<SingList> singLists;
    private Context context;
    private int SELECT_POSITION = -1;

    public Song_List_BaseAdapter(List<SingList> singLists, Context context) {
        this.singLists = singLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return singLists == null ? 0 : singLists.size();
    }

    public void SelectPosition(int position) {
        this.SELECT_POSITION = position;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SongListViewHolder holder;
        if (convertView == null) {
            holder = new SongListViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.song_listview_rc, null);
            holder.play = (ImageView) convertView.findViewById(R.id.song_list_play);
            holder.soucang = (ImageView) convertView.findViewById(R.id.ssong_list_soucang);
            holder.song = (TextView) convertView.findViewById(R.id.song_list_songname);
            holder.singer = (TextView) convertView.findViewById(R.id.song_list_singer);
            convertView.setTag(holder);
        } else {
            holder = (SongListViewHolder) convertView.getTag();
        }
        holder.song.setTextColor(context.getResources().getColor(R.color.text_light));
        holder.singer.setTextColor(context.getResources().getColor(R.color.text_dark));
        holder.song.setText(singLists.get(position).songname);
        holder.singer.setText("-" + singLists.get(position).singername);
        holder.play.setImageDrawable(convertView.getResources().getDrawable(R.drawable.play_list));
        holder.play.setVisibility(View.GONE);
        if (position == SELECT_POSITION) {
            holder.play.setVisibility(View.VISIBLE);
            holder.song.setTextColor(context.getResources().getColor(R.color.red_select));
            holder.singer.setTextColor(context.getResources().getColor(R.color.red_select));
        }
        //为真是可以收藏，为假为已经收藏
        if (!collection_song_query(position)) {
            //已经收藏，显示红色。
            holder.soucang.setImageDrawable(context.getResources().getDrawable(R.drawable.soucang_list));
        } else {
            //为收藏可以点击进行收藏
            holder.soucang.setImageDrawable(context.getResources().getDrawable(R.drawable.soucang_list_1));
        }
        holder.soucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前登录状态
                boolean flag = SharedPreferencesUnitl.getLoginstatus_SharedPreferencesEditor((Activity) context);
                if (flag && collection_song_query(position)) {
                    String username = SharedPreferencesUnitl.getUsername_SharedPreferencesEditor((Activity) context);
                    UserSong_Collection userSong_collection = new UserSong_Collection();
                    SingList song = singLists.get(position);
                    userSong_collection.setBigpic(song.albumpic_small);
                    userSong_collection.setSongname(song.songname);
                    userSong_collection.setSinger(song.singername);
                    if (TextUtils.isEmpty(song.musicurl)) {
                        userSong_collection.setM4aurl(song.m4a);
                    } else {
                        userSong_collection.setM4aurl(song.musicurl);
                    }
                    userSong_collection.setUsername(username);
                    userSong_collection.setCollection_date(new Date());
                    userSong_collection.save();
                    holder.soucang.setImageDrawable(context.getResources().getDrawable(R.drawable.soucang_list));
                    Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                } else if (!flag) {
                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    class SongListViewHolder {
        ImageView play, soucang;
        TextView song, singer;
    }

    private boolean collection_song_query(int position) {
        String songname_collection = singLists.get(position).songname;
        String singer_collection = singLists.get(position).singername;
        //查询是否已经收藏
        int count = DataSupport.where("songname = ? and singer = ?", songname_collection, singer_collection).count(UserSong_Collection.class);
        if (count == 0) {
            return true;
        } else return false;
    }
}
