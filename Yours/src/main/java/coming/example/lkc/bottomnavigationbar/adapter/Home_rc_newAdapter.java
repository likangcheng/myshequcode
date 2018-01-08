package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.HomeCardActivity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;

/**
 * Created by 李康成 on 2018/1/3.
 */

public class Home_rc_newAdapter extends BaseQuickAdapter<JiSuApi_List, BaseViewHolder> {
    private Context context;

    public Home_rc_newAdapter(Context context) {
        super(R.layout.home_rc_layout, null);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final JiSuApi_List item) {
        helper.setText(R.id.news_source, "新浪新闻");
        helper.setText(R.id.news_time, item.NewsTime);
        helper.setText(R.id.news_title, item.NewsTitle);
        if (!TextUtils.isEmpty(item.pic)) {
            Glide.with(context).load(item.pic).into((ImageView) helper.getView(R.id.news_img));
        } else {
            Glide.with(context).load(R.drawable.zwtp111).into((ImageView) helper.getView(R.id.news_img));
        }

    }
}
