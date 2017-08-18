package coming.example.lkc.bottomnavigationbar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.MoviePager_Adapter;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;

/**
 * Created by lkc on 2017/7/31.
 */
public class Movie_Fragment extends Fragment {
    private ViewPager viewPager;
    private int Viewpager_flag = 0;
    private RingProgressBar ringProgressBar;
    private Button button_sb;
    private TextView sb;
    private int progress = 0;
    private int position = 0;
    private static final int VIEWPAGER_TIME = 3500;
    private CirclePageIndicator circlePageIndicator;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    viewPager.setCurrentItem(Viewpager_flag = (Viewpager_flag + 1) % 5);
                    handler.sendEmptyMessageDelayed(0, VIEWPAGER_TIME);
                    break;
                case 1:
                    if (progress > 0) {
                        ringProgressBar.setProgress(progress = progress - 1);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_movie);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.CirclePage_Indicator);
        button_sb = (Button) view.findViewById(R.id.button_sb);
        sb = (TextView) view.findViewById(R.id.sb);
        ringProgressBar = (RingProgressBar) view.findViewById(R.id.ringprogressbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MoviePager_Adapter adapter = new MoviePager_Adapter(getActivity());
        viewPager.setAdapter(adapter);
        circlePageIndicator.setViewPager(viewPager);
        handler.sendEmptyMessageDelayed(0, VIEWPAGER_TIME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progress >= 0 && progress < 100) {
                        Thread.sleep(500);
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                ringProgressBar.setVisibility(View.GONE);
                button_sb.setVisibility(View.GONE);
                sb.setVisibility(View.VISIBLE);
                sb.setText("你共点了" + position + "下\n点到100%的都是傻逼");
            }
        });

        button_sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                ringProgressBar.setProgress(progress = progress + 1);
            }
        });
    }
}
