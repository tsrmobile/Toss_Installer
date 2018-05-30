package th.co.thiensurat.toss_installer.payment.paymentfragment.paymentsuccess;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

public class PaymentSuccessPresenter extends BaseMvpPresenter<PaymentSuccessInterface.View>
        implements PaymentSuccessInterface.Presenter {

    public static PaymentSuccessInterface.Presenter create() {
        return new PaymentSuccessPresenter();
    }
}
