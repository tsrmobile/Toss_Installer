package th.co.thiensurat.toss_installer.itemlist;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItem;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItemGroup;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class ItemlistInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setInstallItemToAdapter(List<InstallItem> installItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<ItemlistInterface.View> {
        void requestInstallItem(String data, String empid);
        void setInstallItemGroup(InstallItemGroup itemGroup);
        InstallItemGroup getInstallItemGroup();
        void setInstallItemToAdapter(InstallItemGroup installGroup);
    }
}
