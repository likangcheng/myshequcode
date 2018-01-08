package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.GameMultiItem;
import coming.example.lkc.bottomnavigationbar.unitl.GlideApp;

/**
 * Created by 李康成 on 2018/1/5.
 */

public class Game_rc_new_Adapter extends BaseMultiItemQuickAdapter<GameMultiItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    private Context context;

    public Game_rc_new_Adapter(Context context) {
        super(null);
        this.context = context;
        addItemType(GameMultiItem.HEADER, R.layout.head_vh_new);
        addItemType(GameMultiItem.ITEM, R.layout.home_item_vh);
        addItemType(GameMultiItem.FOOTER, R.layout.home_footer_vh);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameMultiItem item) {
        switch (helper.getItemViewType()) {
            case GameMultiItem.HEADER:
                GlideApp.with(context).load(item.gameMultiItem_head.getTitle_img()).into((ImageView) helper.getView(R.id.header_vh_new_title));
                String content = "当前有<font color='red'>" + item.gameMultiItem_head.getHead_content() + "</font>个新闻";
                helper.setText(R.id.header_vh_new_date, Html.fromHtml(content));
                helper.setText(R.id.header_vh_new_text, item.gameMultiItem_head.getHead_title());
                helper.setImageResource(R.id.header_vh_new_img, item.gameMultiItem_head.getHead_img());
                break;
            case GameMultiItem.ITEM:
                if (!TextUtils.isEmpty(item.jiSuApi_list.pic)) {
                    Glide.with(context).load(item.jiSuApi_list.pic).into((ImageView) helper.getView(R.id.item_vh_img));
                } else {
                    Glide.with(context).load(R.drawable.zwtp111).into((ImageView) helper.getView(R.id.item_vh_img));
                }
                helper.setText(R.id.item_vh_text, item.jiSuApi_list.NewsTitle);
                helper.setText(R.id.item_vh_time, item.jiSuApi_list.NewsTime);
                break;
            case GameMultiItem.FOOTER:
                break;
        }
    }
}
