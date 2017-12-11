package th.co.thiensurat.toss_installer.detail.edit.addresspayment;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class PaymentAddressPresenter extends BaseMvpPresenter<PaymentAddressInterface.View> implements PaymentAddressInterface.Presenter {

    private DBHelper dbHelper;
    private List<AddressItem> addressItems;

    public static PaymentAddressInterface.Presenter create() {
        return new PaymentAddressPresenter();
    }

    @Override
    public void getAddressDetail(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddressDetail(addressItems);
    }
}
