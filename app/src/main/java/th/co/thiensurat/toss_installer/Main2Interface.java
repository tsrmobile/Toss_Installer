package th.co.thiensurat.toss_installer;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

public class Main2Interface {

    public interface View extends BaseMvpInterface.View {
        void onSuccess();
        //void showNotificationSyncIcon();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<Main2Interface.View> {
        //void getAddressNotSync();
        void updateCurrentLocation(String id, double lat, double lon);
    }
}
