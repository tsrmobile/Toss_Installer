package th.co.thiensurat.toss_installer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import th.co.thiensurat.toss_installer.auth.item.AuthenItem;
import th.co.thiensurat.toss_installer.auth.item.AuthenItemGroup;


/**
 * Created by teera-s on 5/19/2016 AD.
 */
public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "APP";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPreferrence(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferrence(String key) {
        return pref.getString(key, null);
    }

    public void setProfile(AuthenItemGroup authenItemGroup) {
        for (AuthenItem item : authenItemGroup.getData()) {
            editor.putString(Constance.KEY_EMPID, item.getEmployeecode());
            editor.putString(Constance.KEY_TITLE, item.getTitle());
            editor.putString(Constance.KEY_FIRSTNAME, item.getFirstname());
            editor.putString(Constance.KEY_LASTNAME, item.getLastname());
            editor.putString(Constance.KEY_POSITION, item.getPositionName());
            editor.putString(Constance.KEY_DEPARTMENT, item.getDepartmentName());
        }
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
