package coming.example.lkc.bottomnavigationbar.unitl;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.GameMultiItem;
import coming.example.lkc.bottomnavigationbar.dao.GameMultiItem_Foot;
import coming.example.lkc.bottomnavigationbar.dao.GameMultiItem_Head;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;

/**
 * Created by 李康成 on 2018/1/5.
 */

public class GameDataServer {
    private static List<String> Header_text = new ArrayList<>(Arrays.asList("今日头条", "财金报道", "体育快讯", "娱乐杂谈"));
    private static List<String> Header_date = new ArrayList<>(Arrays.asList("1234", "247", "313", "455"));
    private static String[] imgurl = {
            "http://img5.mtime.cn/mg/2018/01/05/092919.97582455.jpg",
            "http://img5.mtime.cn/mg/2018/01/02/130418.51366625.jpg",
            "http://img5.mtime.cn/mg/2018/01/04/094539.46119588.jpg",
            "http://img5.mtime.cn/mg/2018/01/03/093121.90288855.jpg",
    };
    private static List<Integer> Header_img = new ArrayList<>(Arrays.asList(R.drawable.xhl, R.drawable.hg, R.drawable.kdy
            , R.drawable.lmc));

    public static List<GameMultiItem> getGameMultiItem(List<JiSuApi_List> jiSuApi_lists) {
        int size = Header_text.size();
        List<GameMultiItem> get_gameMultiItems = new ArrayList<>();
        //这条语句是防止上次调用该方法时gameMultiItems会再下次调用该方法中累加
        get_gameMultiItems.clear();
        for (int j = 0; j < size; j++) {
            get_gameMultiItems.add(new GameMultiItem(GameMultiItem.HEADER,
                    GameMultiItem.TWO_SIZE,
                    new GameMultiItem_Head(Header_text.get(j), Header_img.get(j), imgurl[j], Header_date.get(j))));
            get_gameMultiItems.addAll(getjiSuApi_lists_2_GameMultiItem(j * 8, (j + 1) * 8, jiSuApi_lists));
            get_gameMultiItems.add(new GameMultiItem(GameMultiItem.FOOTER, GameMultiItem.TWO_SIZE, new GameMultiItem_Foot()));
        }
        return get_gameMultiItems;
    }

    public static List<GameMultiItem> loadmoreGameMultiItem(boolean first, List<JiSuApi_List> jiSuApi_lists) {
        List<GameMultiItem> load_gameMultiItems = new ArrayList<>();
        if (first) {
            load_gameMultiItems.add(new GameMultiItem(GameMultiItem.HEADER,
                    GameMultiItem.TWO_SIZE,
                    new GameMultiItem_Head("猜你喜欢", R.drawable.mwzz, "http://img5.mtime.cn/mg/2017/10/29/091734.32711331.jpg", "很多很多")));
        }
        load_gameMultiItems.addAll(getjiSuApi_lists_2_GameMultiItem(0, 20, jiSuApi_lists));
        return load_gameMultiItems;
    }

    private static List<GameMultiItem> getjiSuApi_lists_2_GameMultiItem(int index, int end,
                                                                        List<JiSuApi_List> jiSuApi_lists) {
        List<GameMultiItem> get2_gameMultiItems = new ArrayList<>();
        for (int i = index; i < end; i++) {
            get2_gameMultiItems.add(new GameMultiItem(GameMultiItem.ITEM, GameMultiItem.ONE_SIZE, jiSuApi_lists.get(i)));
        }
        return get2_gameMultiItems;
    }
}

