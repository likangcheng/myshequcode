package coming.example.lkc.bottomnavigationbar;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;

public class HomeCardActivity extends AppCompatActivity {
    public static final String CONTENTLIST_DATA = "contentlist data";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_card);
        JiSuApi_List jiSuApi_list = (JiSuApi_List) getIntent().getSerializableExtra(CONTENTLIST_DATA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_homecard);
        toolbar.setTitle(jiSuApi_list.NewsTitle);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initfindID();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(jiSuApi_list.NewsUrl);
    }

    private void initfindID() {
        webView = (WebView) findViewById(R.id.home_card_webview);
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
