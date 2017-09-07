package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.HomeCardActivity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;

/**
 * Created by lkc on 2017/7/31.
 */
public class Home_rc_Adapter extends RecyclerView.Adapter<Home_rc_Adapter.ViewHolder> {
    private Context context;
    private List<JiSuApi_List> contentlists;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView newsimg;
        TextView newsTitle, newsTime, newsSource;

        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view;
            newsimg = (ImageView) view.findViewById(R.id.news_img);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsTime = (TextView) view.findViewById(R.id.news_time);
            newsSource = (TextView) view.findViewById(R.id.news_source);

        }
    }

    public Home_rc_Adapter(List<JiSuApi_List> datalist) {
        this.contentlists = datalist;
    }

    @Override
    public Home_rc_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.home_rc_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                JiSuApi_List jiSuApi_list = contentlists.get(position);
                Intent intent = new Intent(context, HomeCardActivity.class);
                intent.putExtra(HomeCardActivity.CONTENTLIST_DATA, jiSuApi_list);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Home_rc_Adapter.ViewHolder holder, int position) {
        JiSuApi_List jiSuApi_list = contentlists.get(position);
        holder.newsSource.setText("新浪新闻");
        holder.newsTime.setText(jiSuApi_list.NewsTime);
        holder.newsTitle.setText(jiSuApi_list.NewsTitle);
        if (!TextUtils.isEmpty(jiSuApi_list.pic)) {
            Glide.with(context).load(jiSuApi_list.pic).into(holder.newsimg);
        } else {
            Glide.with(context).load(R.drawable.unimg).into(holder.newsimg);
        }
    }

    @Override
    public int getItemCount() {
        return contentlists.size();
    }
}
