package th.co.thiensurat.toss_installer.payment.detail;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class CustomerPaymentDetailInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<CustomerPaymentDetailInterface.View> {
        void updateDueDate(String orderid, String duedate);
    }
}
