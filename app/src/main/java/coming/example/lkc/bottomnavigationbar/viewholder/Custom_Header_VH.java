package coming.example.lkc.bottomnavigationbar.viewholder;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by lkc on 2017/9/8.
 */
public class Custom_Header_VH extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView headtext, headdate;

    public Custom_Header_VH(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.header_vh_img);
        headtext = (TextView) itemView.findViewById(R.id.header_vh_text);
        headdate = (TextView) itemView.findViewById(R.id.header_vh_date);
    }

    public void setDate(String text, String date, int imgid, Context context) {
        Glide.with(context).load(imgid).into(imageView);
        headtext.setText(text);
        String content = "当前有<font color='red'>" + date + "</font>个新闻";
        headdate.setText(Html.fromHtml(content));
    }
}
