package coming.example.lkc.bottomnavigationbar.unitl;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by 李康成 on 2018/1/9.
 * 用于检测当前软键盘是否弹起的工具类
 */

public class KeyboardStatusDetector {
    /**
     * SOFT_KEY_BOARD_MIN_HEIGHT
     * 该值为手机主界面以外的所有像素值。
     * 有一个界限超过这个值，就代表有软键盘弹起。这个界限值因各种手机而异，160-200之间应该更合理。
     */
    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 160;
    private KeyboardVisibilityListener mVisibilityListener;

    boolean keyboardVisible = false;

    public void registerFragment(Fragment f) {
        registerView(f.getView());
    }

    public void registerActivity(Activity a) {
        registerView(a.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public KeyboardStatusDetector registerView(final View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);

                int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
/*                Log.d("wode", "r.bottom：" + r.bottom);
                Log.d("wode", "r.top: " + r.top);
                Log.d("wode", "heightDiff: " + heightDiff);
                Log.d("wode", "v.getRootView().getHeight()： " + v.getRootView().getHeight());*/
                if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) { // if more than 100 pixels, its probably a keyboard...
                    if (!keyboardVisible) {
                        keyboardVisible = true;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(true);
                        }
                    }
                } else {
                    if (keyboardVisible) {
                        keyboardVisible = false;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(false);
                        }
                    }
                }
            }
        });

        return this;
    }

    public KeyboardStatusDetector setmVisibilityListener(KeyboardVisibilityListener listener) {
        mVisibilityListener = listener;
        return this;
    }

    public interface KeyboardVisibilityListener {
        void onVisibilityChanged(boolean keyboardVisible);
    }
}
