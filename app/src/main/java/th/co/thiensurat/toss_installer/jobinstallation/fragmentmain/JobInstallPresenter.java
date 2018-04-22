package th.co.thiensurat.toss_installer.jobinstallation.fragmentmain;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertItemToGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobInstallPresenter extends BaseMvpPresenter<JobInstallInterface.View> implements JobInstallInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public static JobInstallInterface.Presenter create(FragmentActivity activity) {
        context = activity;
        return new JobInstallPresenter();
    }

    public JobInstallPresenter() {
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
    public void getAllJob() {
        getView().onLoading();
        serviceManager.requestJob("all", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID), new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    jobItemList = ConvertItemToGroup.creatJobItemList(result.getData());
                    setJobToDB(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDimiss();
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onDimiss();
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDimiss();
                Log.e("onFailure", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void setJobToDB(List<JobItem> jobItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.setTableJob(jobItemList);
        getView().onDimiss();
        getView().onSuccess();
    }
}
