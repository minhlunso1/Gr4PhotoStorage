package group4.gr4photostorage.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.Plus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import group4.gr4photostorage.Application;
import group4.gr4photostorage.R;
import group4.gr4photostorage.helper.AP;

/**
 * Created by Administrator on 22-May-16.
 */
public class LoginActivity extends GoogleBaseActivity {

    private boolean mResolvingError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AP.getStringData(this, "id")!=null)
            checkGooglePlayServicesAvailableAndLogin();
    }

    @OnClick(R.id.btn_login)
    public void checkGooglePlayServicesAvailableAndLogin() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(getApplicationContext(), "Try again.", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            } else {
                Toast.makeText(this, "Please install Google Service.", Toast.LENGTH_LONG).show();
            }
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Application.me = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (Application.me!=null) {
            AP.saveData(this, "id", Application.me.getId());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingError)
            return;
        else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, 1);
                Toast.makeText(this, "Press login again to access", Toast.LENGTH_LONG).show();
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            mResolvingError = true;
            Toast.makeText(this, getString(R.string.please_check_connection), Toast.LENGTH_LONG).show();
        }
    }
}
