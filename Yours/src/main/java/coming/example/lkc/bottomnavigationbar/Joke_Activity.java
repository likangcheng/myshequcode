package coming.example.lkc.bottomnavigationbar;

/*
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;

import java.util.ArrayList;
import java.util.List;

public class Joke_Activity extends MyBaseActivity {
    private ImageView joke_content;
    private Transferee transferee;
    private TransferConfig transferConfig;
    String path = "http://wimg.spriteapp.cn/x/640x400/ugc/2017/11/23/5a16c3382f730_1.jpg";
    private List<String> pathlist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transferee = Transferee.getDefault(this);
        pathlist.add(path);
        setContentView(R.layout.activity_joke);
        inittoolbar();
        initView();
        initTransferConfig();
        Glide.with(this).load(path).apply(new RequestOptions().centerCrop()).into(joke_content);
        joke_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferConfig.setNowThumbnailIndex(0);
                List<ImageView> imageViewList = new ArrayList<>();
                imageViewList.add(joke_content);
                transferConfig.setOriginImageList(imageViewList);
                transferee.apply(transferConfig).show(new Transferee.OnTransfereeStateChangeListener() {
                    @Override
                    public void onShow() {
                        Glide.with(Joke_Activity.this).pauseRequests();
                    }

                    @Override
                    public void onDismiss() {
                        Glide.with(Joke_Activity.this).resumeRequests();
                    }
                });
            }
        });
    }

    private void initTransferConfig() {
        transferConfig = TransferConfig.build()
                .setSourceImageList(pathlist)
                .setMissPlaceHolder(R.mipmap.icon_yours)
                .setErrorPlaceHolder(R.mipmap.icon_yours)
                .setProgressIndicator(new ProgressPieIndicator())
                .setIndexIndicator(new NumberIndexIndicator())
                .setJustLoadHitImage(true)
                .setImageLoader(GlideImageLoader.with(getApplicationContext()))
                .setOnLongClcikListener(new Transferee.OnTransfereeLongClickListener() {
                    @Override
                    public void onLongClick(ImageView imageView, int pos) {
                    }
                })
                .create();
    }

    private void initView() {
        joke_content = (ImageView) findViewById(R.id.joke_content);
    }

    */
/**
     * 实例化toolbar
     *//*

    private void inittoolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_joke);
        toolbar.setTitle("百思不得姐");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
*/
