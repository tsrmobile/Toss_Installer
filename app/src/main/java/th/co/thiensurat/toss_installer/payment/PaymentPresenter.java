package th.co.thiensurat.toss_installer.payment;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 3/9/2018.
 */

public class PaymentPresenter extends BaseMvpPresenter<PaymentInterface.View> implements PaymentInterface.Presenter {

    public static PaymentInterface.Presenter create() {
        return new PaymentPresenter();
    }
}
