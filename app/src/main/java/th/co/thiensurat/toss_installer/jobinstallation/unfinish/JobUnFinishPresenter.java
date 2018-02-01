package th.co.thiensurat.toss_installer.jobinstallation.unfinish;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.ConvertJobList;
import th.co.thiensurat.toss_installer.job.item.JobItem;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobUnFinishPresenter extends BaseMvpPresenter<JobUnFinishInterface.View> implements JobUnFinishInterface.Presenter {

    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public JobUnFinishPresenter() {
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

    public static JobUnFinishInterface.Presenter create() {
        return new JobUnFinishPresenter();
    }

    @Override
    public void getJobUnFinish(String data, String empid) {
        //getView().onLoad();
        serviceManager.requestJobUnSuccess(data, empid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    //getView().onDismiss();
                    jobItemList = ConvertJobList.creatJobItemList(result.getData());
                    getView().setJobItemToAdapter(jobItemList);
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
                Log.e("Failure un success", t.getLocalizedMessage());
            }
        });
    }
}
