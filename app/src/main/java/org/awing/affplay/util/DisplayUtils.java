package org.awing.affplay.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayUtils {
    // px = dp * (density); density = dpi / 160
    // convert dp to pixel
    public static  int dp2px(int dp, Context context)
    {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int)(dm.density * dp);
    }


    public static  int px2dp(int px, Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int)(px / dm.density);
    }

}
