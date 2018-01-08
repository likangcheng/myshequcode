package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.Book_Card_Activity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.WeiXin_Content_list;
import coming.example.lkc.bottomnavigationbar.unitl.GlideApp;

/**
 * Created by lkc on 2017/8/1.
 */
public class Book_rc_Adapter extends BaseQuickAdapter<WeiXin_Content_list, BaseViewHolder> {
    private Context mcontext;

    public Book_rc_Adapter(Context context) {
        super(R.layout.book_rc_layout, null);
        mcontext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiXin_Content_list item) {
        helper.setText(R.id.weixin_time, item.ct.split(" ")[0]);
        helper.setText(R.id.weixin_title, item.weixintitle);
        GlideApp.with(mContext).load(item.contentImg).into((ImageView) helper.getView(R.id.weixin_img));
    }

}
