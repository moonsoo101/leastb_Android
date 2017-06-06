package com.leastb.moonsoo.walkingeye.Util;

import android.content.res.Resources;

/**
 * Created by wisebody on 2017. 6. 5..
 */

public class DpPx {
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
