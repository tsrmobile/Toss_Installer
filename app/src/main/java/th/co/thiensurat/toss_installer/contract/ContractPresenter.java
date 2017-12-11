package th.co.thiensurat.toss_installer.contract;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class ContractPresenter extends BaseMvpPresenter<ContractInterface.View> implements ContractInterface.Presenter {

    private DBHelper dbHelper;
    private List<AddressItem> addressItems;

    public static ContractInterface.Presenter create() {
        return new ContractPresenter();
    }

    @Override
    public void getAddressFromSQLite(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddessFromSQLite(addressItems);
    }
}
