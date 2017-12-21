package coming.example.lkc.bottomnavigationbar.dao;

import org.litepal.crud.DataSupport;

/**
 * Created by lkc on 2017/8/3.
 */
public class Users extends DataSupport {
    private int id;
    private String username;
    private String password;
    private String path;
    private String backimgpath;

    public String getBackimgpath() {
        return backimgpath;
    }

    public void setBackimgpath(String backimgpath) {
        this.backimgpath = backimgpath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
