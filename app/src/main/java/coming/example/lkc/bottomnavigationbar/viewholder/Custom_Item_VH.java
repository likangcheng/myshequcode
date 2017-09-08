package coming.example.lkc.bottomnavigationbar.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;

/**
 * Created by lkc on 2017/9/8.
 */
public class Custom_Item_VH extends RecyclerView.ViewHolder {
    CardView cardView;
    ImageView imageView;
    TextView title, time;

    public Custom_Item_VH(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.item_vh_cardview);
        imageView = (ImageView) itemView.findViewById(R.id.item_vh_img);
        title = (TextView) itemView.findViewById(R.id.item_vh_text);
        time = (TextView) itemView.findViewById(R.id.item_vh_time);
    }

    public void setDate(JiSuApi_List lists, Context context) {
        if (!TextUtils.isEmpty(lists.pic)) {
            Glide.with(context).load(lists.pic).into(imageView);
        } else {
            Glide.with(context).load(R.drawable.zwtp111).into(imageView);
        }
        title.setText(lists.NewsTitle);
        time.setText(lists.NewsTime);
    }
}
