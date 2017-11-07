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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lkc on 2017/10/23.
 */

public class Collection_Song_Adapter extends RecyclerView.Adapter<Collection_Song_Adapter.MyViewHolder> {
    private List<UserSong_Collection> datalist;
    private Context context;
    private Songcount_listener listener_count;
    private OnClicklistener onClicklistener;

    public Collection_Song_Adapter(List<UserSong_Collection> datalist, Context context) {
        this.datalist = datalist;
        this.context = context;
    }

    public interface Songcount_listener {
        void getCount(int position);
    }

    public interface OnClicklistener {
        void OnClick(View v, int position);
    }

    public void setOnPositionClikcListener(OnClicklistener listener) {
        this.onClicklistener = listener;
    }

    public void setgetCountonChangeListener(Songcount_listener listener) {
        this.listener_count = listener;
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
        View view = LayoutInflater.from(context).inflate(R.layout.song_collection_rc, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                onClicklistener.OnClick(view, position);
            }
        });
        return holder;
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
                if (listener_count != null) {
                    listener_count.getCount(datalist.size());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }
}
