package coming.example.lkc.bottomnavigationbar.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Game_Fragment extends Fragment {
    private TextView textView;
    final static String url = "http://www.yikatom.com/sendJsonToApp1";
    final static String biturl = "http://i.gtimg.cn/music/photo/mid_album_90/7/K/001qHmKU29WX7K.jpg";
    private final static String APP_ID = "wxd6ab7c22e73907b9";
    private IWXAPI iwxapi;
    private String responsedata;

    private void regtoWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game, container, false);
        regtoWX(view.getContext());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) getActivity().findViewById(R.id.game_text);
                JSONObject json = new JSONObject();
                try {
                    json.put("key1", "index");
                    json.put("key2", "1");
                    json.put("key3", "2");
                    HttpUnitily.sendOkHttpRequesttoJSON(url, json, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("wode", "onResponse: " + responsedata);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("wode", "onResponse: " );
                            responsedata = response.body().string();
                            Log.d("wode", "onResponse: " + responsedata);

                        }
                    });
                } catch (JSONException e) {
                    Log.d("wode", "onResponse: " + responsedata);
                    e.printStackTrace();
                }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getActivity()).load(biturl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        WXMusicObject wxmusic = new WXMusicObject();
                        wxmusic.musicUrl = "http://ws.stream.qqmusic.qq.com/202773258.m4a?fromtag=46";
                        WXMediaMessage mediaMessage = new WXMediaMessage();
                        mediaMessage.mediaObject = wxmusic;
                        mediaMessage.title = "追光者";
                        mediaMessage.description = "李俊";
                        mediaMessage.thumbData = Bitmap2Bytes(resource);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = mediaMessage;
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                        iwxapi.sendReq(req);
                    }
                });
            }
        });

    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
