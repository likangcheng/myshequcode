package coming.example.lkc.bottomnavigationbar;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import coming.example.lkc.bottomnavigationbar.dao.Contentlist;

public class HomeCardActivity extends AppCompatActivity {
    public static final String CONTENTLIST_DATA="contentlist data";
    private TextView homecard_title,homecard_time,homecard_text;
    private ImageView homecard_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_card);
        Contentlist contentlist = (Contentlist) getIntent().getSerializableExtra(CONTENTLIST_DATA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_homecard);
        toolbar.setTitle(contentlist.getSource());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initfindID();
        homecard_title.setText(contentlist.getNewstitle());
        homecard_time.setText(contentlist.getPubDate());
        homecard_text.setText("  "+contentlist.getNewsinfo());
        if (contentlist.isHavePic()){
            Glide.with(this).load(contentlist.imageurls.get(0).getImgurl()).into(homecard_img);
        }else {
            homecard_img.setVisibility(View.GONE);
        }
    }

    private void initfindID() {
        homecard_text= (TextView) findViewById(R.id.homecard_text);
        homecard_time= (TextView) findViewById(R.id.homecard_time);
        homecard_title= (TextView) findViewById(R.id.homecard_title);
        homecard_img= (ImageView) findViewById(R.id.homecard_img);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
