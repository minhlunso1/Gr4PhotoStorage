package group4.gr4photostorage.helper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import group4.gr4photostorage.Application;

/**
 * Created by Minh on 7/1/2016.
 */
public class QuickBloxHelper {

    public static void initQuickBlox(Context context) {
        QBSettings.getInstance().init(context, Consts.QB_APP_ID, Consts.QB_AUTH_KEY, Consts.QB_AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Consts.QB_ACCOUNT_KEY);
    }

    public void createSessionWithAccount() {
        QBUser qbUser = new QBUser(Application.USER_LOGIN, Application.USER_PASSWORD);
        QBAuth.createSession(qbUser, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });
    }

    public static String getUrlFromQBFile(QBFile qbFile) {
        if (qbFile.isPublic()) {
            String publicUrl = qbFile.getPublicUrl();
            if (!TextUtils.isEmpty(publicUrl)) {
                return publicUrl;
            }
        }

        return qbFile.getPrivateUrl();
    }
}
