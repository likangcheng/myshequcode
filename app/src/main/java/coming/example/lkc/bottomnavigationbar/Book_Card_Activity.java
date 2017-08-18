package coming.example.lkc.bottomnavigationbar;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import coming.example.lkc.bottomnavigationbar.dao.WeiXin_Content_list;

public class Book_Card_Activity extends AppCompatActivity {
    public static final String WEIXIN_DATA = "weixin data";
    public int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__card_);
        WeiXin_Content_list data = (WeiXin_Content_list) getIntent().getSerializableExtra(WEIXIN_DATA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weixin);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_weixin);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageView weixin_card_img = (ImageView) findViewById(R.id.weixin_card_img);
        WebView weixin_webview = (WebView) findViewById(R.id.weixin_content_text);
        collapsingToolbarLayout.setTitle(data.weixintitle);
        Glide.with(this).load(data.contentImg).into(weixin_card_img);
        weixin_webview.getSettings().setJavaScriptEnabled(true);
        weixin_webview.setWebViewClient(new WebViewClient());
        weixin_webview.loadUrl(data.infourl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
