package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by lkc on 2017/8/10.
 */
public class MoviePager_Adapter extends PagerAdapter {
    private Context mcontext;
    private int[] imgdata = {
            R.drawable.item1,
            R.drawable.item2,
            R.drawable.item3,
            R.drawable.item4,
            R.drawable.item5
    };
    private List<View> data = new ArrayList<>();

    public MoviePager_Adapter(Context context) {
        this.mcontext = context;
        for (int i = 0; i < imgdata.length; i++) {
        View view = View.inflate(mcontext, R.layout.viewpager_movie_item, null);
        ImageView imageView = (ImageView) view;
            imageView.setImageResource(imgdata[i]);
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
