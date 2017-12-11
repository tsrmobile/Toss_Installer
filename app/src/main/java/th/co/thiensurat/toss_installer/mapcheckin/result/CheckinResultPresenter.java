package th.co.thiensurat.toss_installer.mapcheckin.result;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class CheckinResultPresenter extends BaseMvpPresenter<CheckinResultInterface.View> implements CheckinResultInterface.Presenter {

    private DBHelper dbHelper;
    private List<ImageItem> imageItemList;
    public static CheckinResultInterface.Presenter create() {
        return new CheckinResultPresenter();
    }

    @Override
    public void getImage(Context context, String orderid, String type) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.imageItemList = dbHelper.getImage(orderid, type);
        getView().setImageToAdapter(imageItemList);
    }
}
