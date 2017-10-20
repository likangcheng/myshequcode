package coming.example.lkc.bottomnavigationbar.other_view

import android.content.Context
import android.widget.Toast

/**
 * Created by lkc on 2017/10/19.
 */
fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}