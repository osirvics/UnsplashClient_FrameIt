package com.free.wallpaper.download.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Victor on 1/21/2017.
 */

public class Util {
    //public static final String APPLICATION_ID = "7a96a77d719e9967f935da53784d6a3eb58a4fb174dda25e89ec69059e46c815";
    public static final String APPLICATION_ID = "2741f6c2d7ac2b0383077350ab3e6541240f6cde75f88356911da837cab7e3e9";
    public static final String ORDER_BY_LATEST = "latest";
    public static final String ORDER_BY_OLDEST = "oldest";
    public static final String ORDER_BY_POPULAR = "popular";
    public static final int PER_PAGE = 10;
    public static final int ALT_PER_PAGE = 11;
    public static final int TWELVE_PER_PAGE = 12;
    public static final String packageName = "com.free.wallpaper.download";
    public static final int RESPONSE_SUCCESS = 200;
    public static final int RESPONSE_RATE_LIMIT_EXCEEDED =403;

    public static void setTypeface(Context c, TextView t) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t.setTypeface(Typeface.createFromAsset(c.getAssets(), "fonts/Courier.ttf"),Typeface.NORMAL);
        }
    }


    public static void openPlayStore(Context c, String appPackName){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + appPackName
            ));
            c.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://details?id=" + appPackName));
            c.startActivity(intent);
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        }
        else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

}
