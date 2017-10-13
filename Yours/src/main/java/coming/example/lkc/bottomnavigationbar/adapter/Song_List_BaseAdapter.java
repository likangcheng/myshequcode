package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.SingList;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        holder.soucang.setImageDrawable(context.getResources().getDrawable(R.drawable.soucang_list_1));
        return convertView;
    }

    class SongListViewHolder {
        ImageView play, soucang;
        TextView song, singer;
    }
}
