package com.domain.codetest_jake_moritz;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import io.realm.Realm;

public class App extends Application {

    private static App mInstance;
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        Realm.init(this);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public Realm getRealm() {
        if (realm == null || realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}
