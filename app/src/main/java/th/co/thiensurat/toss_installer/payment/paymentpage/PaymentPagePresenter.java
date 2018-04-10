package th.co.thiensurat.toss_installer.payment.paymentpage;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class PaymentPagePresenter extends BaseMvpPresenter<PaymentPageInterface.View> implements PaymentPageInterface.Presenter {

    public static PaymentPageInterface.Presenter create() {
        return new PaymentPagePresenter();
    }
}
