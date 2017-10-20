package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/8/10.
 */
public class MoviePager_Adapter extends PagerAdapter {
    private Context mcontext;
    private List<Integer> Title_img = new ArrayList<>(Arrays.asList(R.drawable.aa11, R.drawable.aa12, R.drawable.aa13, R.drawable.aa14, R.drawable.aa15));
    private List<View> data = new ArrayList<>();

    public MoviePager_Adapter(Context context) {
        this.mcontext = context;
//        for (int i = 0; i < Title_img.size(); i++) {
//            View view = View.inflate(mcontext, R.layout.viewpager_movie_item, null);
//            ImageView imageView = (ImageView) view;
//            Glide.with(mcontext).load(Title_img.get(i)).into(imageView);
//            data.add(imageView);
//        }
    }

    public void setDate(List<String> list) {
        if (list != null && list.size() > 0) {
            data.clear();
            for (int i = 0; i < list.size(); i++) {
                View view = View.inflate(mcontext, R.layout.viewpager_movie_item, null);
                ImageView imageView = (ImageView) view;
                Glide.with(mcontext).load(list.get(i)).into(imageView);
                data.add(imageView);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = data.get(position);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "you click position" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
