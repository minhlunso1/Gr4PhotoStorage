package group4.gr4photostorage.helper;

import group4.gr4photostorage.R;
import group4.gr4photostorage.utils.ResourceUtils;

public class Consts {

    public static final String PASS_POSTFIX = "group4_pm";
    public static final String QB_APP_ID = "41093";
    public static final String QB_AUTH_KEY = "zgaNOSNYCdeFk-8";
    public static final String QB_AUTH_SECRET = "2GOJNVcjD7rga9k";
    public static final String QB_ACCOUNT_KEY = "RBLso4vb84heD99iD976";

    public static int PREFERRED_IMAGE_WIDTH_PREVIEW = ResourceUtils.getDimen(R.dimen.item_gallery_width);
    public static int PREFERRED_IMAGE_HEIGHT_PREVIEW = ResourceUtils.getDimen(R.dimen.item_gallery_height);

    public static int PREFERRED_IMAGE_WIDTH_FULL = ResourceUtils.dpToPx(320);
    public static int PREFERRED_IMAGE_HEIGHT_FULL = ResourceUtils.dpToPx(320);

    public static int PRIORITY_MAX_IMAGE_SIZE = 1024 * 1024 * 20;


}