package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.UserSong_Collection;
import coming.example.lkc.bottomnavigationbar.listener.SongCount_Listener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lkc on 2017/10/23.
 */

public class Collection_Song_Adapter extends RecyclerView.Adapter<Collection_Song_Adapter.MyViewHolder> {
    private List<UserSong_Collection> datalist;
    private Context context;
    private SongCount_Listener listener;

    public Collection_Song_Adapter(List<UserSong_Collection> datalist, Context context, SongCount_Listener listener) {
        this.datalist = datalist;
        this.context = context;
        this.listener = listener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView icon;
        TextView song, singer;
        ImageView delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.song_collection_pic);
            song = (TextView) itemView.findViewById(R.id.song_collection_songname);
            singer = (TextView) itemView.findViewById(R.id.song_collection_singer);
            delete = (ImageView) itemView.findViewById(R.id.song_collection_delete);
        }
    }

    @Override
    public Collection_Song_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_collection_rc, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Collection_Song_Adapter.MyViewHolder holder, final int position) {
        holder.singer.setText("-" + datalist.get(position).getSinger());
        holder.song.setText(datalist.get(position).getSongname());
        Glide.with(context).load(datalist.get(position).getBigpic()).into(holder.icon);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(UserSong_Collection.class, "songname = ?", datalist.get(position).getSongname());
                datalist.remove(position);
                listener.getCount(datalist.size());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }
}
