package com.noddyandfriends.disastermanager.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by anant on 29/1/17.
 */

public class Utilities {
    public static int convertDpToPx(Context context, int dp){
        return  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
}
