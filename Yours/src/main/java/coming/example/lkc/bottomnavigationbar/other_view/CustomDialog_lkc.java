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
    private OnDialogMenuClick onDialogMenuClick = null;

    public CustomDialog_lkc(Context context) {
        this.context = context;
        SetDialog();
    }

    public interface OnDialogMenuClick {
        void OnCameraClick(View view);

        void OnAblmClick(View view);

        void OnCancelClick(View view);
    }

    public void setOnDialogMenuClick(OnDialogMenuClick onDialogMenuClick) {
        this.onDialogMenuClick = onDialogMenuClick;
    }

    public void SetDialog() {
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_headpic_layout, null);
        TextView camera = view.findViewById(R.id.dialog_head_camera);
        TextView albm = view.findViewById(R.id.dialog_head_albm);
        TextView cancel = view.findViewById(R.id.dialog_head_cancel);
        camera.setOnClickListener(this);
        albm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_head_camera:
                if (onDialogMenuClick != null) {
                    onDialogMenuClick.OnCameraClick(v);
                }
                dialog.dismiss();
                break;
            case R.id.dialog_head_albm:
                if (onDialogMenuClick != null) {
                    onDialogMenuClick.OnAblmClick(v);
                }
                dialog.dismiss();
                break;
            case R.id.dialog_head_cancel:
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
