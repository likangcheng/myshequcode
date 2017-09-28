package coming.example.lkc.bottomnavigationbar.other_view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by lkc on 2017/9/5.
 */
public class CustomDialog_lkc implements View.OnClickListener {
    private Dialog dialog;
    private Context context;
    private View view1;
    private OnDialogMenuClick onDialogMenuClick = null;

    public CustomDialog_lkc(Context context) {
        this.context = context;
    }

    public interface OnDialogMenuClick {
        void OnOkClick(View view);

        void OnCancelClick(View view);
    }

    public void setOnDialogMenuClick(OnDialogMenuClick onDialogMenuClick) {
        this.onDialogMenuClick = onDialogMenuClick;
    }

    public void SetDialog(String oktext, String title, String canceltext) {
        dialog = new Dialog(context, R.style.PhotographAndPicture_DialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.determine_cancel_dialog_layout, null);
        TextView text = (TextView) view.findViewById(R.id.tcdl_text);
        TextView ok = (TextView) view.findViewById(R.id.tcdl_ok);
        TextView cancel = (TextView) view.findViewById(R.id.tcdl_cancel);
        cancel.setText(canceltext);
        ok.setText(oktext);
        text.setText(title);
        ok.setOnClickListener(this);
        text.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public void SetDialog(String title, String oktext) {
        dialog = new Dialog(context, R.style.PhotographAndPicture_DialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.determine_cancel_dialog_layout, null);
        TextView text = (TextView) view.findViewById(R.id.tcdl_text);
        TextView ok = (TextView) view.findViewById(R.id.tcdl_ok);
        TextView cancel = (TextView) view.findViewById(R.id.tcdl_cancel);
        view1 = view.findViewById(R.id.view_tcdl);
        cancel.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        ok.setText(oktext);
        text.setText(title);
        ok.setTextColor(Color.parseColor("#4A4A4A"));
        ok.setOnClickListener(this);
        text.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tcdl_ok:
                if (onDialogMenuClick != null) {
                    onDialogMenuClick.OnOkClick(v);
                }
                dialog.dismiss();
                break;
            case R.id.tcdl_cancel:
                if (onDialogMenuClick != null) {
                    onDialogMenuClick.OnCancelClick(v);
                }
                dialog.dismiss();
                break;
            default:
                break;
        }
    }
}
