package coming.example.lkc.bottomnavigationbar.dao;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by lkc on 2017/10/20.
 */

public class UserSong_Collection extends DataSupport{
    private int id;
    private String username;
    private String songname;
    private String singer;
    private String m4aurl;
    private String bigpic;
    private String smallpic;
    private Date collection_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getM4aurl() {
        return m4aurl;
    }

    public void setM4aurl(String m4aurl) {
        this.m4aurl = m4aurl;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public String getSmallpic() {
        return smallpic;
    }

    public void setSmallpic(String smallpic) {
        this.smallpic = smallpic;
    }

    public Date getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(Date collection_date) {
        this.collection_date = collection_date;
    }
}
