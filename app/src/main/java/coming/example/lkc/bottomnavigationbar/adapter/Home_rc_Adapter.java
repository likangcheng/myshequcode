package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.HomeCardActivity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.Contentlist;
import coming.example.lkc.bottomnavigationbar.dao.ShowApi;

/**
 * Created by lkc on 2017/7/31.
 */
public class Home_rc_Adapter extends RecyclerView.Adapter<Home_rc_Adapter.ViewHolder> {
    private Context context;
    private List<Contentlist> contentlists;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView newsimg;
        TextView newsTitle,newsTime,newsSource;

        public ViewHolder(View view){
            super(view);
            cardView= (CardView) view;
            newsimg= (ImageView) view.findViewById(R.id.news_img);
            newsTitle= (TextView) view.findViewById(R.id.news_title);
            newsTime= (TextView) view.findViewById(R.id.news_time);
            newsSource= (TextView) view.findViewById(R.id.news_source);

        }
    }

    public Home_rc_Adapter(List<Contentlist> datalist){
        this.contentlists=datalist;
    }

    @Override
    public Home_rc_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.home_rc_layout,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Contentlist content = contentlists.get(position);
                Intent intent=new Intent(context,HomeCardActivity.class);
                intent.putExtra(HomeCardActivity.CONTENTLIST_DATA,content);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Home_rc_Adapter.ViewHolder holder, int position) {
        Contentlist contentlist=contentlists.get(position);
        holder.newsSource.setText(contentlist.getSource());
        holder.newsTime.setText(contentlist.getPubDate());
        holder.newsTitle.setText(contentlist.getNewstitle());
        if (contentlist.isHavePic()){
            Glide.with(context).load(contentlist.imageurls.get(0).getImgurl()).into(holder.newsimg);
        }else {
            Glide.with(context).load(R.drawable.unimg).into(holder.newsimg);
        }
    }

    @Override
    public int getItemCount() {
        return contentlists.size();
    }
}
