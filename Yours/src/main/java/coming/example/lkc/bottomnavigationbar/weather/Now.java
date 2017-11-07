package coming.example.lkc.bottomnavigationbar.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lkc on 2017/7/24.
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }

}
