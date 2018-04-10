package th.co.thiensurat.toss_installer.productwithdraw.fragment;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItem;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItemGroup;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class WithdrawProductInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void onApply();
        void setInstallItemToAdapter(List<InstallItem> installItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void requestInstallItem(String data, String action);
        void requestApplyItem(String data, String action);
        void setInstallItemGroup(InstallItemGroup itemGroup);
        InstallItemGroup getInstallItemGroup();
        void setInstallItemToAdapter(InstallItemGroup installGroup);

        void insertDataToSQLite(List<InstallItem> installItemList);
        boolean checkStockID(String number);
    }
}
