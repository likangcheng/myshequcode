package coming.example.lkc.bottomnavigationbar.unitl;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;

/**
 * Created by 李康成 on 2018/1/8.
 * 首页滚动Banner
 */

public class Head_ViewPager_Get {
    private Banner banner;
    private List<String> list = new ArrayList<>(Arrays.asList("冰雪节神秘礼物大放送", "无限火力雪球大作战正式启动"
            , "冰雪节皮肤50%优惠", "武器大师竞技场之冬季擂台", "英雄联盟搞笑狂欢周开启"));


    public View getView(final Context context, List<String> pic, RecyclerView recyclerView) {
        View view = LayoutInflater.from(context).inflate(R.layout.head_view_page, (ViewGroup) recyclerView.getParent(), false);
        banner = view.findViewById(R.id.banner);
        banner.setImageLoader(new MyImageLoader());
        banner.setImages(pic);
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        banner.setBannerTitles(list);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3500);
        banner.start();
        return view;
    }

    public class MyImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideApp.with(context).load(path).into(imageView);
        }
    }
}
