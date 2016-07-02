package group4.gr4photostorage;

import com.google.android.gms.plus.model.people.Person;
import com.quickblox.sample.core.CoreApp;

/**
 * Created by Administrator on 22-May-16.
 */
public class Application extends CoreApp {
    public static Person me;
    public static String USER_LOGIN;
    public static String USER_PASSWORD;
    public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized Application getInstance() {
        return instance;
    }
}
