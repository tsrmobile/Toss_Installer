package th.co.thiensurat.toss_installer.takepicturecheckin;

import android.content.Context;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class MapCheckinPresenter extends BaseMvpPresenter<MapCheckinInterface.View> implements MapCheckinInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;

    public static MapCheckinInterface.Presenter create(Context activity) {
        context = activity;
        return new MapCheckinPresenter();
    }

    @Override
    public void saveImageUrl(String orderid, String type, String url, String productcode) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.addImage(orderid, "", type, url, productcode);
        getView().resultCheckin();
    }

    @Override
    public void checkImage(String orderid, String type) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.getImage(orderid, type).size() > 0 ) {
            getView().resultCheckin();
        }
    }

    @Override
    public void editImageUrl(String id, String url) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.editImage(id, url);
        getView().resultCheckin();
    }
}
