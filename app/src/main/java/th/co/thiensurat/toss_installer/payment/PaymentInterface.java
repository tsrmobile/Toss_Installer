package th.co.thiensurat.toss_installer.payment;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 3/9/2018.
 */

public class PaymentInterface {

    public interface View extends BaseMvpInterface.View {

    }

    public interface Presenter extends BaseMvpInterface.Presenter<PaymentInterface.View> {

    }
}
