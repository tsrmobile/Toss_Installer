package th.co.thiensurat.toss_installer.takepicturecard;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.takepicture.TakePictureInterface;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/17/2017.
 */

public class TakeIDCardPresenter extends BaseMvpPresenter<TakeIDCardInterface.View> implements TakeIDCardInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private List<ImageItem> imageItemList;

    public static TakeIDCardInterface.Presenter create(Context activity) {
        context = activity;
        return new TakeIDCardPresenter();
    }

    @Override
    public void saveImageUrl(String orderid, String type, String url, String productcode) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.addImage(orderid, "", type, url, productcode);
        getView().refresh();
    }

    @Override
    public void getImage(String orderid, String type) {
        getView().onLoading();
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.imageItemList = dbHelper.getImage(orderid, type);
        getView().setImageToAdapter(imageItemList);
        getView().onDismiss();
    }

    @Override
    public void editImageUrl(String id, String url) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.editImage(id, url);
        getView().refresh();
    }

    @Override
    public void delImage(String id) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.deleteImage(id);
        getView().refresh();
    }

    @Override
    public void updateStep(String orderid, String step) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateStep(orderid, step);
    }
}
