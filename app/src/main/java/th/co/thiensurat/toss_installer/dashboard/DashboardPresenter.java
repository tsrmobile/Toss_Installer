package th.co.thiensurat.toss_installer.dashboard;

import com.hwangjr.rxbus.RxBus;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.DashboardItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.dashboard.item.ConvertDashboardItem;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItem;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItemGroup;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardPresenter extends BaseMvpPresenter<DashboardInterface.View> implements DashboardInterface.Presenter {

    private ServiceManager serviceManager;
    private DashboardItemGroup dashboardGroup;
    private List<DashboardItem> dashboardList;

    public static DashboardInterface.Presenter create() {
        return new DashboardPresenter();
    }

    public DashboardPresenter() {
        serviceManager = ServiceManager.getInstance();
    }

    public void setManager( ServiceManager manager ){
        serviceManager = manager;
    }

    @Override
    public void onViewCreate() {
        RxBus.get().register( this );
    }

    @Override
    public void onViewDestroy() {
        RxBus.get().unregister( this );
    }

    @Override
    public void requestSummary(String data, String empid) {
        getView().onLoad();
        serviceManager.getSummary(data, empid, new ServiceManager.ServiceManagerCallback<DashboardItemResultGroup>() {
            @Override
            public void onSuccess(DashboardItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    getView().onDismiss();
                    DashboardItemGroup dashboardItemGroup = ConvertDashboardItem.createDashboardGroupFromResult(result);
                    dashboardGroup = dashboardItemGroup;
                    setDashboardItemGroup(dashboardGroup);
                    dashboardList = ConvertDashboardItem.createListDashboardFromResult(result.getData());
                    getView().setDashboardToAdapter(dashboardList);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void setDashboardItemGroup(DashboardItemGroup itemGroup) {
        this.dashboardGroup = itemGroup;
    }

    @Override
    public DashboardItemGroup getDashboardItemGroup() {
        return dashboardGroup;
    }

    @Override
    public void setDashboardItemToAdapter(DashboardItemGroup dashboardItemGroup) {
        getView().setDashboardToAdapter(dashboardItemGroup.getData());
    }
}
