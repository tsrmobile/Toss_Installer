package th.co.thiensurat.toss_installer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import th.co.thiensurat.toss_installer.auth.item.AuthenItem;
import th.co.thiensurat.toss_installer.auth.item.AuthenItemGroup;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;


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

    /*public void setJobSelected(JobItem item) {
        editor.putString(Constance.KEY_JOB_ORDERID, item.getOrderid());
        editor.putString(Constance.KEY_JOB_IDCARD, item.getIDCard());
        editor.putString(Constance.KEY_JOB_TITLE, item.getTitle());
        editor.putString(Constance.KEY_JOB_FIRSTNAME, item.getFirstName());
        editor.putString(Constance.KEY_JOB_LASTNAME, item.getLastName());
        editor.putString(Constance.KEY_JOB_CONTACT_PHONE, item.getContactphone());
        editor.putString(Constance.KEY_JOB_PRODUCT_CODE, item.getProductCode());
        editor.putString(Constance.KEY_JOB_PRODUCT_NAME, item.getProductName());
        editor.putString(Constance.KEY_JOB_PRODUCT_QTY, item.getProductQty());
        editor.putString(Constance.KEY_JOB_INSTALLDATESTART, item.getInstallStartDate());
        editor.putString(Constance.KEY_JOB_INSTALLDATEEND, item.getInstallEndDate());
        editor.commit();
    }*/

    /*public JobItem getJobSelection() {
        JobItem jobItem = new JobItem()
                .setOrderid(pref.getString(Constance.KEY_JOB_ORDERID, null))
                .setIDCard(pref.getString(Constance.KEY_JOB_IDCARD, null))
                .setTitle(pref.getString(Constance.KEY_JOB_TITLE, null))
                .setFirstName(pref.getString(Constance.KEY_JOB_FIRSTNAME, null))
                .setLastName(pref.getString(Constance.KEY_JOB_LASTNAME, null))
                .setContactphone(pref.getString(Constance.KEY_JOB_CONTACT_PHONE, null))
                .setProductCode(pref.getString(Constance.KEY_JOB_PRODUCT_CODE, null))
                .setProductName(pref.getString(Constance.KEY_JOB_PRODUCT_NAME, null))
                .setProductQty(pref.getString(Constance.KEY_JOB_PRODUCT_QTY, null))
                .setInstallStartDate(pref.getString(Constance.KEY_JOB_INSTALLDATESTART, null))
                .setInstallEndDate(pref.getString(Constance.KEY_JOB_INSTALLDATEEND, null));
        return jobItem;
    }*/

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
