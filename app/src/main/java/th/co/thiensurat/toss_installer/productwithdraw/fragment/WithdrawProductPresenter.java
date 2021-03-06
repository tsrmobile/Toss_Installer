package th.co.thiensurat.toss_installer.productwithdraw.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.hwangjr.rxbus.RxBus;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.productwithdraw.item.ConvertInstallItem;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItem;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class WithdrawProductPresenter extends BaseMvpPresenter<WithdrawProductInterface.View> implements WithdrawProductInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private ServiceManager serviceManager;
    private InstallItemGroup installItemGroup;
    private List<InstallItem> installItemList;

    public static WithdrawProductInterface.Presenter create(FragmentActivity activity) {
        context = activity;
        return new WithdrawProductPresenter();
    }

    public WithdrawProductPresenter() {
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
    public void requestInstallItem(String data, String action) {
        getView().onLoad();
        serviceManager.loadInstallItem(data, action, new ServiceManager.ServiceManagerCallback<InstallItemResultGroup>() {
            @Override
            public void onSuccess(InstallItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    InstallItemGroup itemGroup = ConvertInstallItem.creatinstallItemGroup(result);
                    installItemGroup = itemGroup;
                    setInstallItemGroup(installItemGroup);

                    installItemList = ConvertInstallItem.creatinstallItemList(result.getData());
                    getView().setInstallItemToAdapter(installItemList);
                    getView().onDismiss();
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
    public void requestApplyItem(final String data, String action) {
        getView().onLoad();
        serviceManager.loadInstallItem(data, action, new ServiceManager.ServiceManagerCallback<InstallItemResultGroup>() {
            @Override
            public void onSuccess(InstallItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    getView().onDismiss();
                    getView().onApply();
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
    public void setInstallItemGroup(InstallItemGroup itemGroup) {
        this.installItemGroup = itemGroup;
    }

    @Override
    public InstallItemGroup getInstallItemGroup() {
        return installItemGroup;
    }

    @Override
    public void setInstallItemToAdapter(InstallItemGroup installGroup) {
        getView().setInstallItemToAdapter(installGroup.getData());
    }

    @Override
    public void insertDataToSQLite(List<InstallItem> installItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.setTableItem(installItemList);
    }

    @Override
    public boolean checkStockID(String number) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.checkNumber(number);
    }
}
