package th.co.thiensurat.toss_installer.payment.paymentpage;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class PaymentPageInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void onDueSuccess(String success);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<PaymentPageInterface.View> {
        void addToLocalDB(List<RequestPayment.paymentBody> paymentBodies);
        void addContractPayment(List<RequestPayment.paymentBody> paymentBodies);
        void updateDueDate(String orderid, String duedate, String empid);
    }
}
