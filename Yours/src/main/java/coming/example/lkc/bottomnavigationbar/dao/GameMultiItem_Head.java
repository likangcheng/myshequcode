package coming.example.lkc.bottomnavigationbar.dao;

import android.content.Intent;

/**
 * Created by 李康成 on 2018/1/5.
 */

public class GameMultiItem_Head {
    private String head_title;
    private int head_img;
    private String title_img;
    private String head_content;

    public GameMultiItem_Head(String head_title, int head_img, String title_img, String head_content) {
        this.head_title = head_title;
        this.head_img = head_img;
        this.title_img = title_img;
        this.head_content = head_content;
    }

    public String getHead_title() {
        return head_title;
    }


    public int getHead_img() {
        return head_img;
    }


    public String getTitle_img() {
        return title_img;
    }


    public String getHead_content() {
        return head_content;
    }


}
