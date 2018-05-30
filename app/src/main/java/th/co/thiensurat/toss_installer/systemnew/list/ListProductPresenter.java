package th.co.thiensurat.toss_installer.systemnew.list;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertItemToGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

public class ListProductPresenter extends BaseMvpPresenter<ListProductInterface.View> implements ListProductInterface.Presenter {

    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public static ListProductInterface.Presenter create() {
        return new ListProductPresenter();
    }

    public ListProductPresenter() {
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
    public void getList(String data, String empid) {
        //getView().onLoad();
        serviceManager.getListProduct(data, empid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    //getView().onDismiss();
                    jobItemList = ConvertItemToGroup.creatJobItemList(result.getData());
                    getView().setListToAdapter(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    //getView().onDismiss();
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    //getView().onDismiss();
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //getView().onDismiss();
                Log.e("Failure current job", t.getLocalizedMessage());
            }
        });
    }
}
