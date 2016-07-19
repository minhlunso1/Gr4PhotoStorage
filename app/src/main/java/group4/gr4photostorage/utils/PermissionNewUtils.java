package group4.gr4photostorage.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group4.gr4photostorage.R;
import group4.gr4photostorage.helper.AlertDialogHelper;

/**
 * Created by Minh on 7/17/2016.
 */
public class PermissionNewUtils {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    public void checkPermission(final AppCompatActivity activity, final IDo iDo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            final List<String> permissionsNeeded = new ArrayList<String>();

            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(activity, permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add(Manifest.permission.CAMERA);
            if (!addPermission(activity, permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionsList.size() > 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }else { //for clarity
                iDo.doWhat();
            }
        } else {
            iDo.doWhat();
        }
    }

    private boolean addPermission(AppCompatActivity activity, List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(permission);
        return true;
    }

    public void onRequestPermissionsResult(final AppCompatActivity activity, int requestCode, String[] permissions, int[] grantResults, final IDo iDo) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    iDo.doWhat();
                } else {
                    // Permission Denied
                    AlertDialogHelper.showWithFullCase(activity, activity.getString(R.string.Deny_permission_try_again),
                            activity.getString(R.string.Cancel), activity.getString(R.string.Try_again), new AlertDialogHelper.IDialogDo() {
                                @Override
                                public void doWhat() {
                                    activity.finish();
                                }
                            }, new AlertDialogHelper.IDialogDo() {
                                @Override
                                public void doWhat() {
                                    checkPermission(activity, iDo);
                                }
                            });
                }
            }
            break;
        }
    }

    public interface IDo {
        void doWhat();
    }
}
