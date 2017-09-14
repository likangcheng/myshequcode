package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by lkc on 2017/8/10.
 */
public class MoviePager_Adapter extends PagerAdapter {
    private Context mcontext;
    private List<Integer> Title_img = new ArrayList<>(Arrays.asList(R.drawable.aa11, R.drawable.aa12, R.drawable.aa13, R.drawable.aa14, R.drawable.aa15));
    private String[] imgurl = {
            "http://img5.mtime.cn/mg/2017/09/14/090931.42802398.jpg",
            "http://img5.mtime.cn/mg/2017/09/13/090510.61509534.jpg",
            "http://img5.mtime.cn/mg/2017/09/12/092258.23134801.jpg",
            "http://img5.mtime.cn/mg/2017/09/10/092952.73981337.jpg",
            "http://img5.mtime.cn/mg/2017/09/09/090018.96429511.jpg"
    };
    private List<View> data = new ArrayList<>();

    public MoviePager_Adapter(Context context) {
        this.mcontext = context;
        for (int i = 0; i < Title_img.size(); i++) {
            View view = View.inflate(mcontext, R.layout.viewpager_movie_item, null);
            ImageView imageView = (ImageView) view;
            Glide.with(mcontext).load(Title_img.get(i)).into(imageView);
            data.add(imageView);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = data.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = data.get(position);
        container.removeView(view);
    }
}
