package coming.example.lkc.bottomnavigationbar;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by 李康成 on 2017/11/1.
 */

public class About_Version_Activity extends MyBaseActivity {
    private final static String APP_ID = "wxd6ab7c22e73907b9";//微信ID
    private IWXAPI iwxapi;

    private void regtoWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutversion_layout);
        regtoWX(this);
        TextView tv = (TextView) findViewById(R.id.yours_version);
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            tv.setText("The Yours App Version：" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Button share_button = (Button) findViewById(R.id.share_app);
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAPPtoWX();
            }
        });
    }

    private void shareAPPtoWX() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WXWebpageObject wxWebpageObject = new WXWebpageObject();
                wxWebpageObject.webpageUrl = "https://www.pgyer.com/THve";
                WXMediaMessage wxMediaMessage = new WXMediaMessage();
                wxMediaMessage.title = "Yours 一款烂到爆的APP";
                wxMediaMessage.description = "这是一款BUG很多、听歌没版权、新闻过时的APP，作者人蠢不帅还缺心眼。";
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_yours);
                wxMediaMessage.thumbData = Bitmap2Bytes(bitmap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wxMediaMessage;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                iwxapi.sendReq(req);

            }
        }).start();

    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
