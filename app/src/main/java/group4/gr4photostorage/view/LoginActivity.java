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
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import butterknife.ButterKnife;
import butterknife.OnClick;
import group4.gr4photostorage.Application;
import group4.gr4photostorage.R;
import group4.gr4photostorage.helper.AP;
import group4.gr4photostorage.helper.Consts;
import group4.gr4photostorage.helper.QuickBloxHelper;
import group4.gr4photostorage.utils.PermissionNewUtils;

/**
 * Created by Administrator on 22-May-16.
 */
public class LoginActivity extends GoogleBaseActivity {

    private boolean mResolvingError;
    private PermissionNewUtils permissionNewUtils;
    private LoginActivity activity;
    private final int RC_RESOLVE = 1;

    PermissionNewUtils.IDo iDo = new PermissionNewUtils.IDo() {
        @Override
        public void doWhat() {
            showProgressDialogGr4();
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
            if (status != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, 0);
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Toast.makeText(getApplicationContext(), "Try again.", Toast.LENGTH_LONG).show();
                            closeProgressDialogGr4();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(getBaseContext(), "Please install Google Service.", Toast.LENGTH_LONG).show();
                    closeProgressDialogGr4();
                }
            } else {
                mGoogleApiClient.connect();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        activity = this;
        permissionNewUtils = new PermissionNewUtils();
        QuickBloxHelper.initQuickBlox(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AP.getStringData(this, "id")!=null)
            checkGooglePlayServicesAvailableAndLogin();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeProgressDialogGr4();
    }

    @OnClick(R.id.btn_login)
    public void checkGooglePlayServicesAvailableAndLogin() {
        permissionNewUtils.checkPermission(this, iDo);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionNewUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults, iDo);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Application.me = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (Application.me!=null) {
            AP.saveData(this, "id", Application.me.getId());

            final QBUser user = new QBUser();
            user.setLogin(Application.me.getId());

            QBAuth.createSession(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession session, Bundle params) {
                    loginQB(user);
                }

                @Override
                public void onError(QBResponseException error) {
                }
            });

        }
    }

    private void loginQB(QBUser user) {
        user.setPassword(Application.me.getId()+Consts.PASS_POSTFIX);
        QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Application.USER_LOGIN = user.getLogin();
                Application.USER_PASSWORD = Application.me.getId()+Consts.PASS_POSTFIX;
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(QBResponseException errors) {
                if (errors.getMessage().equals("Unauthorized"))
                    registerQB();
            }
        });
    }

    private void registerQB() {
        final QBUser user = new QBUser();
        user.setLogin(Application.me.getId());
        user.setPassword(Application.me.getId()+Consts.PASS_POSTFIX);
        user.setFullName(Application.me.getDisplayName());
        QBUsers.signUp(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                loginQB(user);
            }

            @Override
            public void onError(QBResponseException error) {
                System.out.println(error.getMessage());
                closeProgressDialogGr4();
            }
        });
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
                connectionResult.startResolutionForResult(this, RC_RESOLVE);
                closeProgressDialogGr4();
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            mResolvingError = true;
            Toast.makeText(this, getString(R.string.please_check_connection), Toast.LENGTH_LONG).show();
            closeProgressDialogGr4();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_RESOLVE) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    showProgressDialogGr4();
                    mGoogleApiClient.connect();
                }
            }
        }
    }

}
