package th.co.thiensurat.toss_installer.mapcheckin;

import android.content.Context;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class MapCheckinPresenter extends BaseMvpPresenter<MapCheckinInterface.View> implements MapCheckinInterface.Presenter {

    private DBHelper dbHelper;

    public static MapCheckinInterface.Presenter create() {
        return new MapCheckinPresenter();
    }

    @Override
    public void saveImageUrl(Context context, String orderid, String type, String url) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.addImage(orderid, "", type, url);
        getView().resultCheckin();
    }

    @Override
    public void checkImage(Context context, String orderid, String type) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.getImage(orderid, type).size() > 0 ) {
            getView().resultCheckin();
        }
    }
}
