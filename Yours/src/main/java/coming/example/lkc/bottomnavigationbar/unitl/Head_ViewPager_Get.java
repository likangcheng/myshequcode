package coming.example.lkc.bottomnavigationbar.unitl;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.MoviePager_Adapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 李康成 on 2018/1/8.
 */

public class Head_ViewPager_Get {
    private static int Viewpager_flag = 0;
    private static final int VIEWPAGER_TIME = 3500;
    private static Handler handler = new Handler() {
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
    private static MoviePager_Adapter adapter;
    private static ViewPager viewPager;


    public static View getView(Context context, List<String> pic, RecyclerView recyclerView) {
        View view = LayoutInflater.from(context).inflate(R.layout.head_view_page, (ViewGroup) recyclerView.getParent(), false);
        viewPager = view.findViewById(R.id.new_head_viewpager);
        PageIndicatorView pageIndicatorView = view.findViewById(R.id.new_pageIndicatorView);
        adapter = new MoviePager_Adapter(context);
        viewPager.setAdapter(adapter);
        pageIndicatorView.setAnimationType(AnimationType.WORM);
        pageIndicatorView.setViewPager(viewPager);
        handler.sendEmptyMessageDelayed(0, VIEWPAGER_TIME);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        adapter.setDate(pic);
        return view;
    }

    public static void setData(List<String> pic) {
        adapter.setDate(pic);
    }
}
