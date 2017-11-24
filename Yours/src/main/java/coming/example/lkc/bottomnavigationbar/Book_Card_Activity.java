package coming.example.lkc.bottomnavigationbar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import coming.example.lkc.bottomnavigationbar.adapter.Fenxaing_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.WeiXin_Content_list;

public class Book_Card_Activity extends MyBaseActivity implements View.OnClickListener {
    private final static String APP_ID = "wxd6ab7c22e73907b9";
    private IWXAPI iwxapi;
    public static final String WEIXIN_DATA = "weixin data";
    private WeiXin_Content_list data;
    private Dialog dialog;

    private void regtoWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__card_);
        regtoWX(this);
        initdata();//BOOK界面点击内容
        initfenxiang();//微信分享
    }

    private void initfenxiang() {
        RelativeLayout fenxiangimg = (RelativeLayout) findViewById(R.id.book_card_fenxiang);
        fenxiangimg.setOnClickListener(this);
    }

    private void fenxaingDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.fenxiang_dialog_layout, null);
        TextView copyurl = (TextView) view.findViewById(R.id.fengxiang_copy);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        RecyclerView FXrecyclerView = (RecyclerView) view.findViewById(R.id.fenxiang_dialog_rc);
        copyurl.setOnClickListener(this);
        cancel.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        FXrecyclerView.setLayoutManager(layoutManager);
        Fenxaing_Adapter adapter = new Fenxaing_Adapter(Book_Card_Activity.this);
        FXrecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new Fenxaing_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        WXfengxiang(0);
                        dialog.dismiss();
                        break;
                    case 1:
                        WXfengxiang(1);
                        dialog.dismiss();
                        break;
                }
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void WXfengxiang(final int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject object = new WXWebpageObject();
                Log.d("wode", "onResourceReady: " + data.contentImg);
                object.webpageUrl = data.infourl;
                WXMediaMessage message = new WXMediaMessage(object);
                message.title = data.weixintitle;
                message.description = "来自你的社区APP最新的微信文章精选";
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(Book_Card_Activity.this).load(data.userLogo).asBitmap().into(100, 100).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                message.thumbData = Bitmap2Bytes(bitmap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = message;
                if (flag == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                iwxapi.sendReq(req);
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_card_fenxiang:
                fenxaingDialog();
                break;
            case R.id.dialog_cancel:
                dialog.dismiss();
                break;
        }
    }

    private void initdata() {
        data = (WeiXin_Content_list) getIntent().getSerializableExtra(WEIXIN_DATA);
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

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        Log.d("wode", "Bitmap2Bytes: ");
        return baos.toByteArray();
    }
}
