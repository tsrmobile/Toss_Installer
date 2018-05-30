package th.co.thiensurat.toss_installer.deposit.deposit;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.deposit.item.DepositItem;

public class DepositInterface {

    public interface View extends BaseMvpInterface.View {
        void setActualToAdapter(List<DepositItem> depositItems);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<DepositInterface.View> {
        void getAllActual();
    }
}
