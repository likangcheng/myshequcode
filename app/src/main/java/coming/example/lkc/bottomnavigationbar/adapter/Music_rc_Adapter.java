package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.SingList;
import coming.example.lkc.bottomnavigationbar.service.MusicService;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lkc on 2017/8/7.
 */
public class Music_rc_Adapter extends RecyclerView.Adapter<Music_rc_Adapter.ViewHolder> {
    private Context context;
    private List<SingList> singLists;
    private OnclickMusicData onclickMusicData = null;
    private OnLongClick onLongClick = null;


    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView sing, singer;
        CircleImageView circleImageView;

        public ViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view;
            sing = (TextView) view.findViewById(R.id.music_rc_sing);
            singer = (TextView) view.findViewById(R.id.music_rc_singer);
            circleImageView = (CircleImageView) view.findViewById(R.id.music_rc_icon);
        }
    }

    public void setMusicData(List<SingList> singLists) {
        if (this.singLists == null) {
            this.singLists = singLists;
        } else {
            this.singLists.clear();
            this.singLists = singLists;
        }
        notifyDataSetChanged();
    }

    public static interface OnclickMusicData {
        void MusicData(int Position);
    }

    public interface OnLongClick {
        void FengXiang(View v, int Position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.music_rc_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (onLongClick != null) {
                    onLongClick.FengXiang(v, position);
                }
                return true;
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (onclickMusicData != null) {
                    onclickMusicData.MusicData(position);
                }
            }
        });
        return holder;
    }

    public void setOnItemClickListener(OnclickMusicData musicData) {
        this.onclickMusicData = musicData;
    }

    public void setOnLongItemClickListenter(OnLongClick longClick) {
        this.onLongClick = longClick;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SingList singList = singLists.get(position);
        holder.sing.setText(singList.songname);
        holder.singer.setText(singList.singername);
        Glide.with(context).load(singList.albumpic_small).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return singLists == null ? 0 : singLists.size();
    }
}
