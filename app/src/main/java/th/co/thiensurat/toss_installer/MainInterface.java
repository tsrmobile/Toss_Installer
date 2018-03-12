package th.co.thiensurat.toss_installer;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class MainInterface {

    public interface View extends BaseMvpInterface.View {
        void onSuccess();
        void showNotificationSyncIcon();
        void setAddressSync(List<RequestUpdateAddress.updateBody> updateBodyList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<MainInterface.View> {
        void getAddressNotSync();
        void requestAddressSync(List<RequestUpdateAddress.updateBody> updateBodyList);
    }
}
