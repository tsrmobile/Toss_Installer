package th.co.thiensurat.toss_installer.payment.detail;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class PaymentDetailInterface {

    public interface View extends BaseMvpInterface.View {
        void onPrint();
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void onDueSuccess(String success);
        void setReceiptNumber(String receiptNumber);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<PaymentDetailInterface.View> {
        void updateDueDate(String orderid, String duedate, String empid);
        //void setAddressDetail(String orderid, List<AddressItem> addressItemList);
        void getReceiptNumber(String contno);
        void addToLocalDB(List<RequestPayment.paymentBody> paymentBodies);
        void addContractPayment(List<RequestPayment.paymentBody> paymentBodies);
    }
}
