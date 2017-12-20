package coming.example.lkc.bottomnavigationbar.unitl;

import android.util.Log;

/**
 * Created by 李康成 on 2017/11/10.
 */

public class Logwrite {
    public static void LOG(String string) {
        Log.d("wode", "" + string);
    }

    //    float[] hight;
   /* public float[] getHightAVG(float[] scores, float[] hight) {
        float sum = 0.0f;
        if (scores == null || scores.length < 1) {
            return null;
        } else {
            //求和
            for (int i = 0; i < scores.length; i++) {
                sum += scores[i];
            }
            //求均
            float avg = sum / scores.length;
            int k = 0;
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] > avg) {
                    hight[k] = scores[i];
                    k++;
                }
            }
            return hight;
        }
    }*/
}
