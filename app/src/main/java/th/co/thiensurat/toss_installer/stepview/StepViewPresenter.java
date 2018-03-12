package th.co.thiensurat.toss_installer.stepview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 2/14/2018.
 */

public class StepViewPresenter extends BaseMvpPresenter<StepViewInterface.View> implements StepViewInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private List<String> strings = new ArrayList<String>();

    public static StepViewInterface.Presenter create(Context activity) {
        context = activity;
        return new StepViewPresenter();
    }

    @Override
    public void getStepValue(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        getView().setValueToAdapter(dbHelper.getAllStep(orderid));
    }

    @Override
    public boolean checkStep(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.checkStepCreate(orderid);
    }

    @Override
    public void setProductToTable(String orderid, List<ProductItem> productItems) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.setTableProductByOrderid(orderid, productItems);
    }
}
