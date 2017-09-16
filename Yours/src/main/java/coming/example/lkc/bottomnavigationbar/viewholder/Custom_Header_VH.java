package coming.example.lkc.bottomnavigationbar.viewholder;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Game_rc_Apapter;
import coming.example.lkc.bottomnavigationbar.adapter.MoviePager_Adapter;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;

/**
 * Created by lkc on 2017/9/8.
 */
public class Custom_Header_VH extends RecyclerView.ViewHolder {
    ImageView imageView, titleimg;
    TextView headtext, headdate;
    ViewPager viewPager;
    private int Viewpager_flag = 0;
    private static final int VIEWPAGER_TIME = 3500;
    private CirclePageIndicator circlePageIndicator;
    private RelativeLayout viewpager_RL;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    viewPager.setCurrentItem(Viewpager_flag = (Viewpager_flag + 1) % 5);
                    handler.sendEmptyMessageDelayed(0, VIEWPAGER_TIME);
                    break;
                default:
                    break;
            }
        }
    };

    public Custom_Header_VH(View itemView, Context context) {
        super(itemView);
        titleimg = (ImageView) itemView.findViewById(R.id.header_vh_title);
        imageView = (ImageView) itemView.findViewById(R.id.header_vh_img);
        headtext = (TextView) itemView.findViewById(R.id.header_vh_text);
        headdate = (TextView) itemView.findViewById(R.id.header_vh_date);
        viewPager = (ViewPager) itemView.findViewById(R.id.viewpager_movie);
        circlePageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.CirclePage_Indicator);
        viewpager_RL = (RelativeLayout) itemView.findViewById(R.id.viewpager_rl);
        initViewPagerAdapter(context);
    }

    private void initViewPagerAdapter(Context context) {
        MoviePager_Adapter adapter = new MoviePager_Adapter(context);
        viewPager.setAdapter(adapter);
        circlePageIndicator.setViewPager(viewPager);
        handler.sendEmptyMessageDelayed(0, VIEWPAGER_TIME);
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Viewpager_flag = position;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, VIEWPAGER_TIME);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setDate(String text, String date, int imgid, String titleurl, int section, Context context) {
        if (section == 0) {
            viewpager_RL.setVisibility(View.VISIBLE);
        } else {
            viewpager_RL.setVisibility(View.GONE);
        }
        Glide.with(context).load(imgid).into(imageView);
        Glide.with(context).load(titleurl).into(titleimg);
        headtext.setText(text);
        String content = "当前有<font color='red'>" + date + "</font>个新闻";
        headdate.setText(Html.fromHtml(content));
    }
}
