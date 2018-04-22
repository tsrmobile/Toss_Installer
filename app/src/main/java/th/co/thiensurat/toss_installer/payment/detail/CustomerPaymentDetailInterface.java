package th.co.thiensurat.toss_installer.payment.detail;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class CustomerPaymentDetailInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setReceiptNumber(String receiptNumber);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<CustomerPaymentDetailInterface.View> {
        void updateDueDate(String orderid, String duedate, String empid);
        void setAddressDetail(String orderid, List<AddressItem> addressItemList);
        void getReceiptNumber();
    }
}
