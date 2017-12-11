package th.co.thiensurat.toss_installer.contract;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.AddressItem;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class ContractInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddessFromSQLite(List<AddressItem> itemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<ContractInterface.View> {
        void getAddressFromSQLite(Context context, String orderid);
    }
}
