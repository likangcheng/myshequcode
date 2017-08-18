package coming.example.lkc.bottomnavigationbar.unitl;

import com.google.gson.Gson;

import coming.example.lkc.bottomnavigationbar.dao.Music;
import coming.example.lkc.bottomnavigationbar.dao.ShowApi;
import coming.example.lkc.bottomnavigationbar.dao.WeiXinNew;

/**
 * Created by lkc on 2017/7/31.
 */
public class Utility {
    public static ShowApi handelNewsResponse(String response) {
        Gson gson = new Gson();
        ShowApi showApi = gson.fromJson(response, ShowApi.class);
        return showApi;
    }

    public static WeiXinNew handelWeiXinResponse(String response) {
        Gson gson = new Gson();
        WeiXinNew weiXinNew = gson.fromJson(response, WeiXinNew.class);
        return weiXinNew;
    }

    public static Music handelMusicResponse(String response) {
        Gson gson = new Gson();
        Music music = gson.fromJson(response, Music.class);
        return music;
    }
}
