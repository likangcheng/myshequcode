package coming.example.lkc.bottomnavigationbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

import coming.example.lkc.bottomnavigationbar.unitl.SharedPreferencesUnitl;

/**
 * Created by lkc on 2017/8/22.
 */
public class Start_Activity extends MyBaseActivity {
    private ViewPager viewPager;
    private ImageView[] imageView;
    private Button button;
    private LinePageIndicator circlePageIndicator;
    private int img[] = {
            R.drawable.img1, R.drawable.img2, R.drawable.img0
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDK>21的用户全屏显示
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_start);
        viewPager = (ViewPager) findViewById(R.id.start_viewpager);
        button = (Button) findViewById(R.id.start_button);
        circlePageIndicator = (LinePageIndicator) findViewById(R.id.start_indicator);
        viewPager.setAdapter(new MyAdapter());
        circlePageIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUnitl.PutFirstOpen_SharedPreferencesEditor(Start_Activity.this, false);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    class MyAdapter extends PagerAdapter {

        public MyAdapter() {
            imageView = new ImageView[img.length];
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageView[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (imageView[position] == null) {
                //如果imageview为空，则实例化它
                imageView[position] = new ImageView(Start_Activity.this);
                imageView[position].setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView[position].setImageResource(img[position]);
            }

            container.addView(imageView[position]);
            return imageView[position];
        }

        @Override
        public int getCount() {
            return imageView.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
