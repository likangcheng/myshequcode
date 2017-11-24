package coming.example.lkc.bottomnavigationbar.other_view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import coming.example.lkc.bottomnavigationbar.R;

/**
 * Created by 李康成 on 2017/11/21.
 */

public class AlertDIalog {
    private Dialog dialog;
    private OnAdialogClickListener listener = null;
    public static final int ONLY_DETERMINE = 1;
    public static final int DETERMINE_CANCEL = 2;

    public interface OnAdialogClickListener {
        void OnClick(DialogInterface dialogInterface);
    }

    public AlertDIalog(Context context, String title, String message, int mode) {
        switch (mode) {
            case ONLY_DETERMINE:
                only_determine(context, title, message);
                break;
            case DETERMINE_CANCEL:
                determine_cancel(context, title, message);
                break;
        }
    }

    private void only_determine(Context context, String title, String message) {
        dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.tips)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.OnClick(dialogInterface);
                    }
                }).create();
    }

    private void determine_cancel(Context context, String title, String message) {
        dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.tips)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.OnClick(dialogInterface);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

    }

    public void setOnDiaglogClickListener(OnAdialogClickListener onAdialogClickListener) {
        this.listener = onAdialogClickListener;
    }

    public void DissmissDialog() {
        dialog.dismiss();
    }

    public void ShowDialog() {
        dialog.show();
    }
}
