package coming.example.lkc.bottomnavigationbar.wxapi;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import coming.example.lkc.bottomnavigationbar.R;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;
    private final static String APP_ID = "wxd6ab7c22e73907b9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        iwxapi.handleIntent(getIntent(), this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_WX);
        toolbar.setTitle("微信分享");
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                finish();
                //正确返回
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                Toast.makeText(WXEntryActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(WXEntryActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                //认证被否决
                finish();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                Toast.makeText(WXEntryActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                //发送失败
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                Toast.makeText(WXEntryActivity.this, "不支持错误", Toast.LENGTH_SHORT).show();
                //不支持错误
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                Toast.makeText(WXEntryActivity.this, "一般错误", Toast.LENGTH_SHORT).show();
                //一般错误
                finish();
                break;
            default:
                Toast.makeText(WXEntryActivity.this, "其他不可名状的情况", Toast.LENGTH_SHORT).show();
                //其他不可名状的情况
                finish();
                break;
        }

    }
}
