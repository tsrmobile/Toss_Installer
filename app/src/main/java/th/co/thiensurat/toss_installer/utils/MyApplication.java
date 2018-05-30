package th.co.thiensurat.toss_installer.utils;

import android.app.Application;
import android.content.Intent;

import th.co.thiensurat.toss_installer.Main2Activity;

/**
 * Created by teera-s on 5/19/2016 AD.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    private MyPreferenceManager pref;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }

    public void startService() {
        Intent intent = new Intent(mInstance, LocationMonitoringService.class);
        startService(intent);
    }
}
