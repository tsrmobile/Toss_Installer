package th.co.thiensurat.toss_installer.deposit.channel;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

public class DepositChannelInterface {

    public interface View extends BaseMvpInterface.View {

    }

    public interface Presenter extends BaseMvpInterface.Presenter<DepositChannelInterface.View> {
        String getRef2();
    }
}
