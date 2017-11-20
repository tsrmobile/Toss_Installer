package th.co.thiensurat.toss_installer.itemlist;

import com.hwangjr.rxbus.RxBus;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.itemlist.item.ConvertInstallItem;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItem;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItemGroup;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class ItemlistPresenter extends BaseMvpPresenter<ItemlistInterface.View> implements ItemlistInterface.Presenter {

    private ServiceManager serviceManager;
    private InstallItemGroup installItemGroup;
    private List<InstallItem> installItemList;

    public static ItemlistInterface.Presenter create() {
        return new ItemlistPresenter();
    }

    public ItemlistPresenter() {
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
    public void requestInstallItem(String data, String empid) {
        getView().onLoad();
        serviceManager.loadInstallItem(data, empid, new ServiceManager.ServiceManagerCallback<InstallItemResultGroup>() {
            @Override
            public void onSuccess(InstallItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    InstallItemGroup itemGroup = ConvertInstallItem.creatinstallItemGroup(result);
                    installItemGroup = itemGroup;
                    setInstallItemGroup(installItemGroup);

                    installItemList = ConvertInstallItem.creatinstallItemList(result.getData());
                    getView().setInstallItemToAdapter(installItemList);
                    getView().onDismiss();
                } else if (result.getMessage().equals("FAIL")) {
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
}
