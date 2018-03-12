package th.co.thiensurat.toss_installer.jobinstallation.current;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertJobList;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class CurrentJobPresenter extends BaseMvpPresenter<CurrentJobInterface.View> implements CurrentJobInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public CurrentJobPresenter() {
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

    public static CurrentJobInterface.Presenter create(FragmentActivity fragmentActivity) {
        context = fragmentActivity;
        return new CurrentJobPresenter();
    }

    @Override
    public void getCurrentJob(String data, String empid) {
        serviceManager.requestJob(data, empid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    jobItemList = ConvertJobList.creatJobItemList(result.getData());
                    setJobToTable(jobItemList);
                    getView().setJobItemToAdapter(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure current job", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getCurrentJobLocalDB() {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        getView().setJobItemToAdapter(dbHelper.getJob("21"));
    }

    @Override
    public void setJobToTable(List<JobItem> jobItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.setTableJob(jobItemList);
    }
}
