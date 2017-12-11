package th.co.thiensurat.toss_installer.detail.edit.addresscard;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class CardAddressPresenter extends BaseMvpPresenter<CardAddressInterface.View> implements CardAddressInterface.Presenter {

    private DBHelper dbHelper;
    private List<AddressItem> addressItems;

    public static CardAddressInterface.Presenter create() {
        return new CardAddressPresenter();
    }

    @Override
    public void getAddressDetail(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddressDetail(addressItems);
    }
}
