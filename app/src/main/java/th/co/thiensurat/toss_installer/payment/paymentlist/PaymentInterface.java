package th.co.thiensurat.toss_installer.payment.paymentlist;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

/**
 * Created by teerayut.k on 3/9/2018.
 */

public class PaymentInterface {

    public interface View extends BaseMvpInterface.View {
        void onFail(String fail);
        void setJobPaymentToAdapter(List<JobItem> jobItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<PaymentInterface.View> {
        void requestJobPayment(String data, String empid);
    }
}
