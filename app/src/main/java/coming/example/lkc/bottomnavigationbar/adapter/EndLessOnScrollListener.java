package coming.example.lkc.bottomnavigationbar.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by lkc on 2017/7/27.
 */
public class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    //声明一个LinearLayoutManager
    private GridLayoutManager mgridLayoutManager;

    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    public EndLessOnScrollListener(GridLayoutManager gridLayoutManager) {
        this.mgridLayoutManager = gridLayoutManager;
        Log.d("wode", "EndLessOnScrollListener: " + previousTotal);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mgridLayoutManager.getItemCount();
        firstVisibleItem = mgridLayoutManager.findFirstVisibleItemPosition();
        if (loading) {
            Log.d("wode", "firstVisibleItem: " + firstVisibleItem);
            Log.d("wode", "totalPageCount:" + totalItemCount);
            Log.d("wode", "visibleItemCount:" + visibleItemCount);
            Log.d("wode", "" + previousTotal);

            if (totalItemCount > previousTotal) {
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        //这里需要好好理解
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++;
            Log.d("wode", "onScrolled: " + currentPage);
            onLoadMore(currentPage);
            loading = true;
        }
    }


    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     */
    public void onLoadMore(int currentPage) {
    }

}
