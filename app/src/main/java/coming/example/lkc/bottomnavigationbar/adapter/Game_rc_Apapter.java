package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
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
    private Context context;
    private List<JiSuApi_List> jiSuApi_lists0;
    private List<Integer> Header_img = new ArrayList<>(Arrays.asList(R.drawable.xhl, R.drawable.hg, R.drawable.kdy
            , R.drawable.lmc, R.drawable.mwzz));
    private List<String> Header_text = new ArrayList<>(Arrays.asList("今日头条", "财金报道", "体育快讯", "娱乐杂谈", "女性秘密"));
    private List<String> Header_date = new ArrayList<>(Arrays.asList("1111", "247", "313", "555", "666"));

    public Game_rc_Apapter(Context context) {
        this.context = context;
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

    @Override
    protected int getSectionCount() {
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
        return new Custom_Header_VH(view);
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
        holder.setDate(Header_text.get(section), Header_date.get(section), Header_img.get(section), context);

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
                break;
            case 1:
                JiSuApi_List jiSuApi_list1 = jiSuApi_lists0.get(8 + position);
                holder.setDate(jiSuApi_list1, context);
                break;
            case 2:
                JiSuApi_List jiSuApi_list2 = jiSuApi_lists0.get(14 + position);
                holder.setDate(jiSuApi_list2, context);
                break;
            case 3:
                JiSuApi_List jiSuApi_list3 = jiSuApi_lists0.get(22 + position);
                holder.setDate(jiSuApi_list3, context);
                break;
            case 4:
                JiSuApi_List jiSuApi_list4 = jiSuApi_lists0.get(28 + position);
                holder.setDate(jiSuApi_list4, context);
                break;
            default:
                break;
        }

    }
}
