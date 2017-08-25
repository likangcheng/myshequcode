package coming.example.lkc.bottomnavigationbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by lkc on 2017/8/25.
 */
public class Fenxaing_Adapter extends RecyclerView.Adapter<Fenxaing_Adapter.ViewHolder> {
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;
    private int[] fximg = {
            R.drawable.fxwx, R.drawable.fxpyq, R.drawable.fxqq,
            R.drawable.fxqzone, R.drawable.fxwb, R.drawable.fxzfb
    };
    private String[] fxname = {
            "微信好友", "微信朋友圈", "QQ", "QQ空间", "微博", "支付宝",
    };

    public Fenxaing_Adapter(Context context) {
        this.context = context;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.fenxaing_rc, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(fximg[position]);
        holder.textView.setText(fxname[position]);
    }

    @Override
    public int getItemCount() {
        return fximg.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.fx_rc_name);
            imageView = (ImageView) view.findViewById(R.id.fx_rc_img);
        }
    }
}
