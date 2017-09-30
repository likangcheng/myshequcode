package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by lkc on 2017/9/30.
 */

public class Suggest_list_BaseAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;

    public Suggest_list_BaseAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;

    }

    @Override
    public int getCount() {
        if (data != null) {
            if (data.size() > 10) {
                return 10;
            } else {
                return data.size();
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        List_ViewHolder holder;
        if (convertView == null) {
            holder = new List_ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.suggest_listview_rc, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.suggest_icon);
            holder.text = (TextView) convertView.findViewById(R.id.sugggets_text);
            holder.cancel = (ImageView) convertView.findViewById(R.id.suggest_cancel);
            convertView.setTag(holder);
        } else {
            holder = (List_ViewHolder) convertView.getTag();
        }
        holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.his));
        holder.cancel.setImageDrawable(context.getResources().getDrawable(R.drawable.his_cancel));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.text.setText(data.get(position));
        return convertView;
    }

    class List_ViewHolder {
        ImageView icon, cancel;
        TextView text;
    }
}
