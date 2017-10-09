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
import java.util.zip.Inflater;

import coming.example.lkc.bottomnavigationbar.Book_Card_Activity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.WeiXin_Content_list;

/**
 * Created by lkc on 2017/8/1.
 */
public class Book_rc_Adapter extends RecyclerView.Adapter<Book_rc_Adapter.ViewHolder> {
    private Context mcontext;
    private List<WeiXin_Content_list> weiXin_content_lists;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView weixin_img;
        TextView weixin_title, weixin_time;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            weixin_img = (ImageView) view.findViewById(R.id.weixin_img);
            weixin_title = (TextView) view.findViewById(R.id.weixin_title);
            weixin_time = (TextView) view.findViewById(R.id.weixin_time);
        }
    }

    public void setBookData(List<WeiXin_Content_list> datalist) {
        if (weiXin_content_lists == null) {
            this.weiXin_content_lists = datalist;
        } else {
            this.weiXin_content_lists.clear();
            this.weiXin_content_lists = datalist;
        }
        notifyDataSetChanged();
    }

    public void loadmoreBookData(List<WeiXin_Content_list> datalist) {
        if (weiXin_content_lists != null) {
            int page = weiXin_content_lists.size();
            weiXin_content_lists.addAll(datalist);
            notifyItemInserted(page);
        }
    }

    @Override
    public Book_rc_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.book_rc_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                WeiXin_Content_list sd = weiXin_content_lists.get(position);
                Intent intent = new Intent(mcontext, Book_Card_Activity.class);
                intent.putExtra(Book_Card_Activity.WEIXIN_DATA, sd);
                mcontext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Book_rc_Adapter.ViewHolder holder, int position) {
        WeiXin_Content_list weixinclass = weiXin_content_lists.get(position);
        String[] timedata = weixinclass.ct.split(" ");
        holder.weixin_time.setText(timedata[0]);
        holder.weixin_title.setText(weixinclass.weixintitle);
        Glide.with(mcontext).load(weixinclass.contentImg).centerCrop().into(holder.weixin_img);
    }

    @Override
    public int getItemCount() {
        return weiXin_content_lists == null ? 0 : weiXin_content_lists.size();
    }
}
