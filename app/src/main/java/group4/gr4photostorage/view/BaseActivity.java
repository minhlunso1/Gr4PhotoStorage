package group4.gr4photostorage.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.quickblox.auth.QBAuth;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.sample.core.ui.activity.CoreBaseActivity;
import com.quickblox.sample.core.utils.DialogUtils;

import java.util.Date;

import group4.gr4photostorage.R;

/**
 * Created by Minh on 7/1/2016.
 */
public class BaseActivity extends CoreBaseActivity {

    protected ActionBar actionBar;
    protected ProgressDialog progressDialog;
    private String TOKEN = "token";
    private String DATE = "date";

    private ProgressDialog progressDialogGr4;

    public void showProgressDialogGr4() {
        progressDialogGr4 = new ProgressDialog(this, R.style.Theme_Gr4Dialog);
        progressDialogGr4.setIndeterminateDrawable(new FadingCircle());
        progressDialogGr4.setIndeterminate(true);
        progressDialogGr4.setCancelable(false);
        progressDialog.setContentView(R.layout.gr4_progress_dialog);
        progressDialogGr4.show();
    }

    public void closeProgressDialogGr4() {
        if (progressDialogGr4 != null && progressDialogGr4.isShowing()) {
            progressDialogGr4.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        progressDialog = DialogUtils.getProgressDialog(this);
        if (savedInstanceState != null) {
            restoreSession(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        try {
            savedInstanceState.putString(TOKEN, QBAuth.getBaseService().getToken());
            savedInstanceState.putSerializable(DATE, QBAuth.getBaseService().getTokenExpirationDate());
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
    }

    public void restoreSession(Bundle savedInstanceState) {
        try {
            String token = savedInstanceState.getString(TOKEN);
            Date date = (Date) savedInstanceState.getSerializable(DATE);

            QBAuth.createFromExistentToken(token, date);
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
    }
}
