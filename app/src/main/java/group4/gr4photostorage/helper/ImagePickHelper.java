package group4.gr4photostorage.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Minh on 7/2/2016.
 */
public class ImagePickHelper {
    public void pickAnImage(Fragment fragment, int requestCode) {
        ImagePickHelperFragment imagePickHelperFragment = ImagePickHelperFragment.start(fragment, requestCode);
        showImageSourcePickerDialog(fragment.getChildFragmentManager(), imagePickHelperFragment);
    }

    public void pickAnImage(FragmentActivity activity, int requestCode) {
        ImagePickHelperFragment imagePickHelperFragment = ImagePickHelperFragment.start(activity, requestCode);
        showImageSourcePickerDialog(activity.getSupportFragmentManager(), imagePickHelperFragment);
    }

    private void showImageSourcePickerDialog(FragmentManager fm, ImagePickHelperFragment fragment) {
        ImageSourcePickDialogFragment.show(fm,
                new ImageSourcePickDialogFragment.LoggableActivityImageSourcePickedListener(fragment));
    }
}
