package th.co.thiensurat.toss_installer.dashboard;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItem;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItemGroup;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setDashboardToAdapter(List<DashboardItem> dashboardItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<DashboardInterface.View> {
        void requestSummary(String data, String empid);
        void setDashboardItemGroup(DashboardItemGroup itemGroup);
        DashboardItemGroup getDashboardItemGroup();
        void setDashboardItemToAdapter(DashboardItemGroup dashboardItemGroup);
    }
}
