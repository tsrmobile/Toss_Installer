package th.co.thiensurat.toss_installer.payment.paymentitem;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

public class PaymentItemInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<PaymentItemInterface.View> {
        void updateDueDate(String orderid, String duedate, String empid);
    }
}
