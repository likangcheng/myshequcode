package coming.example.lkc.bottomnavigationbar.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;
import coming.example.lkc.bottomnavigationbar.dao.JiSuApi_List;
import coming.example.lkc.bottomnavigationbar.viewholder.Custom_Footer_VH;
import coming.example.lkc.bottomnavigationbar.viewholder.Custom_Header_VH;
import coming.example.lkc.bottomnavigationbar.viewholder.Custom_Item_VH;

/**
 * Created by lkc on 2017/9/8.
 */
public class Game_rc_Apapter extends SectionedRecyclerViewAdapter<Custom_Header_VH, Custom_Item_VH
        , Custom_Footer_VH> {
    private Activity context;
    private List<JiSuApi_List> jiSuApi_lists0;
    private List<Integer> Header_img = new ArrayList<>(Arrays.asList(R.drawable.xhl, R.drawable.hg, R.drawable.kdy
            , R.drawable.lmc, R.drawable.mwzz));
    private List<String> Img_url = new ArrayList<>(Arrays.asList(
            "http://imgs.aixifan.com/cms/2017_03_03/1488523586727.gif",
            "http://imgs.aixifan.com/cms/2017_03_03/1488523474487.gif",
            "http://imgs.aixifan.com/cms/2017_03_03/1488523606418.gif",
            "http://imgs.aixifan.com/cms/2017_03_03/1488523679990.gif",
            "http://imgs.aixifan.com/cms/2017_03_03/1488523695622.gif"
    ));
    private List<String> Header_text = new ArrayList<>(Arrays.asList("今日头条", "财金报道", "体育快讯", "娱乐杂谈", "女性秘密"));
    private List<String> Header_date = new ArrayList<>(Arrays.asList("1111", "247", "313", "455", "696"));
    private String[] imgurl = {
            "http://img5.mtime.cn/mg/2017/10/13/113414.29878320.jpg",
            "http://img5.mtime.cn/mg/2017/10/12/092745.43278082.jpg",
            "http://img5.mtime.cn/mg/2017/10/10/094057.75267757.jpg",
            "http://img5.mtime.cn/mg/2017/10/09/095014.10028933.jpg",
            "http://img5.mtime.cn/mg/2017/10/08/090813.97621772.jpg"
    };

    public Game_rc_Apapter(Activity context) {
        this.context = context;
    }


    @Override
    protected int getSectionCount() {
        Log.d("wode", "getSectionCount: ");
        if (jiSuApi_lists0 == null) {
            return 0;
        } else {
            return 5;
        }
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (jiSuApi_lists0 == null) {
            return 0;
        } else {
            if (section == 0) {
                return 8;
            } else if (section == 1) {
                return 6;
            } else if (section == 2) {
                return 8;
            } else if (section == 3) {
                return 6;
            } else {
                return 10;
            }
        }
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected Custom_Header_VH onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_header_vh, parent, false);
        Log.d("wode", "onCreateSectionHeaderViewHolder: ");
        return new Custom_Header_VH(view, context);
    }

    @Override
    protected Custom_Footer_VH onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_footer_vh, parent, false);
        return new Custom_Footer_VH(view);
    }

    @Override
    protected Custom_Item_VH onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_vh, parent, false);
        return new Custom_Item_VH(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(Custom_Header_VH holder, int section) {
        holder.setDate(Header_text.get(section), Header_date.get(section), Header_img.get(section), imgurl[section], section, context);

    }

    @Override
    protected void onBindSectionFooterViewHolder(Custom_Footer_VH holder, int section) {

    }


    @Override
    protected void onBindItemViewHolder(Custom_Item_VH holder, int section, int position) {
        switch (section) {
            case 0:
                JiSuApi_List jiSuApi_list0 = jiSuApi_lists0.get(position);
                holder.setDate(jiSuApi_list0, context);
                holder.setOnClick(jiSuApi_list0, context);
                break;
            case 1:
                JiSuApi_List jiSuApi_list1 = jiSuApi_lists0.get(8 + position);
                holder.setDate(jiSuApi_list1, context);
                holder.setOnClick(jiSuApi_list1, context);
                break;
            case 2:
                JiSuApi_List jiSuApi_list2 = jiSuApi_lists0.get(14 + position);
                holder.setDate(jiSuApi_list2, context);
                holder.setOnClick(jiSuApi_list2, context);
                break;
            case 3:
                JiSuApi_List jiSuApi_list3 = jiSuApi_lists0.get(22 + position);
                holder.setDate(jiSuApi_list3, context);
                holder.setOnClick(jiSuApi_list3, context);
                break;
            case 4:
                JiSuApi_List jiSuApi_list4 = jiSuApi_lists0.get(28 + position);
                holder.setDate(jiSuApi_list4, context);
                holder.setOnClick(jiSuApi_list4, context);
                break;
            default:
                break;
        }

    }

    public void GameAdapterSetData(List<JiSuApi_List> list0) {
        if (jiSuApi_lists0 == null) {
            this.jiSuApi_lists0 = list0;
        } else {
            jiSuApi_lists0.clear();
            jiSuApi_lists0 = list0;
        }
        notifyDataSetChanged();
    }
}
