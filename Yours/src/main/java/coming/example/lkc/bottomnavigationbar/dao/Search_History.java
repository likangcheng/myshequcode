package coming.example.lkc.bottomnavigationbar.dao;

import org.litepal.crud.DataSupport;

/**
 * Created by lkc on 2017/9/30.
 */

public class Search_History extends DataSupport {
    private int id;
    private String history;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
