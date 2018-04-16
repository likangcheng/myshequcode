package coming.example.lkc.bottomnavigationbar.dao;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李康成 on 2018/1/5.
 */

public class GameMultiItem implements MultiItemEntity {
    //ItemType 分别时头、项、尾
    public static final int HEADER = 1;
    public static final int ITEM = 2;
    public static final int FOOTER = 3;
    //SpanSize 占用的比例
    public static final int ONE_SIZE = 1;
    public static final int TWO_SIZE = 2;
    public static final int TREE_SIZE = 3;
    private int itemType;
    private int spanSize;
    public GameMultiItem_Head gameMultiItem_head;
    public JiSuApi_List jiSuApi_list;
    public GameMultiItem_Foot gameMultiItem_foot;

    /**
     * @param itemType           headtype
     * @param spanSize           headsize
     * @param gameMultiItem_head 头数据类
     */
    public GameMultiItem(int itemType, int spanSize, GameMultiItem_Head gameMultiItem_head) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.gameMultiItem_head = gameMultiItem_head;
    }

    /**
     * @param itemType     itemtype
     * @param spanSize     itemsize
     * @param jiSuApi_list 项数据类
     */
    public GameMultiItem(int itemType, int spanSize, JiSuApi_List jiSuApi_list) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.jiSuApi_list = jiSuApi_list;
    }

    /**
     * @param itemType           foottype
     * @param spanSize           footsize
     * @param gameMultiItem_foot 尾部数据类
     */
    public GameMultiItem(int itemType, int spanSize, GameMultiItem_Foot gameMultiItem_foot) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.gameMultiItem_foot = gameMultiItem_foot;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
