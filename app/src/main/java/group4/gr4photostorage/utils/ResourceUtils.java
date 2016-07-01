package group4.gr4photostorage.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import group4.gr4photostorage.Application;

public class ResourceUtils {

    public static String getString(@StringRes int stringId) {
        return Application.getInstance().getString(stringId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return Application.getInstance().getResources().getDrawable(drawableId);
    }

    public static int getColor(@ColorRes int colorId) {
        return Application.getInstance().getResources().getColor(colorId);
    }

    public static int getDimen(@DimenRes int dimenId) {
        return (int) Application.getInstance().getResources().getDimension(dimenId);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
