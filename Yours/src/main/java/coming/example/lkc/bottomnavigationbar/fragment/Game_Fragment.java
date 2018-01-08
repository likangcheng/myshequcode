package coming.example.lkc.bottomnavigationbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.HomeCardActivity;
import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.adapter.Game_rc_Apapter;
import coming.example.lkc.bottomnavigationbar.adapter.Game_rc_new_Adapter;
import coming.example.lkc.bottomnavigationbar.dao.GameMultiItem;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_Body;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;
import coming.example.lkc.bottomnavigationbar.other_view.CustomDialog;
import coming.example.lkc.bottomnavigationbar.unitl.GameDataServer;
import coming.example.lkc.bottomnavigationbar.unitl.Head_ViewPager_Get;
import coming.example.lkc.bottomnavigationbar.unitl.HttpUnitily;
import coming.example.lkc.bottomnavigationbar.unitl.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lkc on 2017/7/31.
 */
public class Game_Fragment extends Fragment {
    private RecyclerView game_recyclerview;
    private Game_rc_new_Adapter game_rc_new_adapter;
    private SwipeRefreshLayout game_swip;
    private String picurl = "https://raw.githubusercontent.com/likangcheng/myshequcode/master/json/picture";
    private List<GameMultiItem> gameMultiItems = new ArrayList<>();
    private int page = 0;
    private boolean loadmore_first = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game, container, false);
        game_recyclerview = view.findViewById(R.id.game_rcview);
        game_swip = view.findViewById(R.id.game_swiplayout);
        game_swip.setColorSchemeResources(R.color.colorAccent, R.color.bule, R.color.orange, R.color.teal);
        showAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        game_swip.post(new Runnable() {
            @Override
            public void run() {
                game_swip.setRefreshing(true);
                requestNews();
                getpic(0);
            }
        });
        game_swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gameMultiItems.clear();
                game_rc_new_adapter.setEnableLoadMore(false);
                loadmore_first = true;
                requestNews();
                getpic(1);
            }
        });

    }

    private void requestNews() {
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=NBA&start=0&num=40&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            game_swip.setRefreshing(false);
                            game_rc_new_adapter.setEnableLoadMore(true);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final JiSuApi_Body jiSuApi_body = Utility.handelNewsResponse(newsResponse);
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiSuApi_body != null) {
                                if (jiSuApi_body.status.equals("0")) {
                                    gameMultiItems.addAll(GameDataServer.getGameMultiItem(jiSuApi_body.result.Newslist));
                                    game_rc_new_adapter.setNewData(GameDataServer.getGameMultiItem(jiSuApi_body.result.Newslist));
//                                    initPic2Json(jiSuApi_body);
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "请检测网络是否连接正常", Toast.LENGTH_SHORT).show();
                            }
                            game_swip.setRefreshing(false);
                            game_rc_new_adapter.setEnableLoadMore(true);
                        }
                    });
                }
            }
        });
    }

    private void loadmoreNews(int start) {
        String NewsUrl = "http://api.jisuapi.com/news/get?channel=NBA&start=" + start + "&num=20&appkey=9a46b272586356ee";
        HttpUnitily.sendOkHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "获取信息失败请检查网络状况", Toast.LENGTH_SHORT).show();
                            game_rc_new_adapter.loadMoreFail();
                            page--;
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String newsResponse = response.body().string();
                final JiSuApi_Body jiSuApi_body = Utility.handelNewsResponse(newsResponse);
                if (!call.isCanceled()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jiSuApi_body != null) {
                                if (jiSuApi_body.status.equals("0")) {
                                    List<GameMultiItem> gameMultiItems1 = GameDataServer.loadmoreGameMultiItem(loadmore_first, jiSuApi_body.result.Newslist);
                                    gameMultiItems.addAll(gameMultiItems1);
                                    game_rc_new_adapter.addData(gameMultiItems1);
                                    loadmore_first = false;
                                } else {
                                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                            game_rc_new_adapter.loadMoreComplete();
                        }
                    });
                }
            }
        });
    }

    private void getpic(final int flag) {

        HttpUnitily.sendOkHttpRequest(picurl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "广告数据异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JsonString = response.body().string();
                final List<String> picstring = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(JsonString);
                    picstring.add(jsonObject.getString("pic_1"));
                    picstring.add(jsonObject.getString("pic_2"));
                    picstring.add(jsonObject.getString("pic_3"));
                    picstring.add(jsonObject.getString("pic_4"));
                    picstring.add(jsonObject.getString("pic_5"));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (flag == 0) {
                                game_rc_new_adapter.removeAllHeaderView();
                                game_rc_new_adapter.addHeaderView(Head_ViewPager_Get.getView(getActivity(), picstring, game_recyclerview));
                                game_recyclerview.setAdapter(game_rc_new_adapter);
                            } else {
                                Head_ViewPager_Get.setData(picstring);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAdapter() {
        game_rc_new_adapter = new Game_rc_new_Adapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        game_rc_new_adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return game_rc_new_adapter.getData().get(position).getSpanSize();
            }
        });
        game_rc_new_adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (game_rc_new_adapter.getData().get(position).getItemType()) {
                    case GameMultiItem.HEADER:
                        break;
                    case GameMultiItem.ITEM:
                        Intent intent = new Intent(getActivity(), HomeCardActivity.class);
                        intent.putExtra(HomeCardActivity.CONTENTLIST_DATA, game_rc_new_adapter.getData().get(position).jiSuApi_list);
                        getActivity().startActivity(intent);
                        break;
                    case GameMultiItem.FOOTER:
                        break;
                }
            }
        });
        game_rc_new_adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                loadmoreNews(page * 20);
            }
        }, game_recyclerview);
        game_recyclerview.setLayoutManager(layoutManager);
        game_recyclerview.setAdapter(game_rc_new_adapter);
    }


}
