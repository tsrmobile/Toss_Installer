package th.co.thiensurat.toss_installer.productbalance.fragment;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/30/2017.
 */

public class ItemBalancePresenter extends BaseMvpPresenter<ItemBalanceInterface.View> implements  ItemBalanceInterface.Presenter{

    private DBHelper dbHelper;
    private List<InstallItem> installItemList;

    public static ItemBalanceInterface.Presenter create() {
        return new ItemBalancePresenter();
    }

    @Override
    public void getItemBalance(Context context) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        getView().setItemBalanceToAdapter(dbHelper.getItemBalance());
    }
}
