package th.co.thiensurat.toss_installer.jobinstallation.finish;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertJobList;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobFinishPresenter extends BaseMvpPresenter<JobFinishInterface.View> implements JobFinishInterface.Presenter {

    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public JobFinishPresenter() {
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

    public static JobFinishInterface.Presenter create() {
        return new JobFinishPresenter();
    }

    @Override
    public void getJobFinish(String data, String empid) {
        serviceManager.requestJob(data, empid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    jobItemList = ConvertJobList.creatJobItemList(result.getData());
                    getView().setJobItemToAdapter(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure", t.getLocalizedMessage());
            }
        });
    }
}
