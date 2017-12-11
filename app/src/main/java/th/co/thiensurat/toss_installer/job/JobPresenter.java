package th.co.thiensurat.toss_installer.job;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.thiensurat.toss_installer.api.ApiService;
import th.co.thiensurat.toss_installer.api.ApiURL;
import th.co.thiensurat.toss_installer.api.Service;
import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.DashboardItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.ConvertJobList;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class JobPresenter extends BaseMvpPresenter<JobInterface.View> implements JobInterface.Presenter {

    private DBHelper dbHelper;
    private JobItemGroup jobItemGroup;
    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public static JobInterface.Presenter create() {
        return new JobPresenter();
    }

    public JobPresenter() {
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
    public void Jobrequest(String data, String empid, String location) {
        getView().onLoad();
        serviceManager.getJob(data, empid, location, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    getView().onDismiss();
                    JobItemGroup itemGroup = ConvertJobList.creatJobItemGroup(result);
                    jobItemGroup = itemGroup;
                    setJobItemGroup(jobItemGroup);
                    jobItemList = ConvertJobList.creatJobItemList(result.getData());
                    getView().setNewDataToSQLite(jobItemList);
                    //getView().setJobItemToAdapter(jobItemList);
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
                getView().onDismiss();
            }
        });
    }

    @Override
    public void setJobItemGroup(JobItemGroup itemGroup) {
        this.jobItemGroup = itemGroup;
    }

    @Override
    public JobItemGroup getJobItemGroup() {
        return jobItemGroup;
    }

    @Override
    public void setJobItemToAdapter(JobItemGroup jobItemGroup) {
        getView().setJobItemToAdapter(jobItemGroup.getData());
    }

    /*@Override
    public void insertDataToSQLite(Context context, List<JobItem> jobItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.isTableExists(Constance.TABLE_JOB) || dbHelper.isTableExists(Constance.TABLE_ADDRESS)
                || dbHelper.isTableExists(Constance.TABLE_PRODUCT) || dbHelper.isTableExists(Constance.TABLE_IMAGE)) {
            dbHelper.emptyTable(Constance.TABLE_JOB);
            dbHelper.emptyTable(Constance.TABLE_ADDRESS);
            dbHelper.emptyTable(Constance.TABLE_PRODUCT);
            dbHelper.emptyTable(Constance.TABLE_IMAGE);
        }
        dbHelper.setTableJob(jobItemList);
        dbHelper.setTableAddress(jobItemList);
        dbHelper.setTableProduct(jobItemList);
    }*/

    @Override
    public void getJobFromSqlite(Context context, String date) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.jobItemGroup = dbHelper.getJobList(date);
        setJobItemGroup(jobItemGroup);

        jobItemList = jobItemGroup.getData();
        getView().setJobItemToAdapter(jobItemList);
    }

    @Override
    public void insertNewData(Context context, List<JobItem> jobItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.isTableExists(Constance.TABLE_JOB) || dbHelper.isTableExists(Constance.TABLE_ADDRESS)
                || dbHelper.isTableExists(Constance.TABLE_PRODUCT) || dbHelper.isTableExists(Constance.TABLE_IMAGE)) {
            dbHelper.emptyTable(Constance.TABLE_JOB);
            dbHelper.emptyTable(Constance.TABLE_ADDRESS);
            dbHelper.emptyTable(Constance.TABLE_PRODUCT);
            dbHelper.emptyTable(Constance.TABLE_IMAGE);
        }

        dbHelper.setTableJob(jobItemList);
        dbHelper.setTableAddress(jobItemList);
        dbHelper.setTableProduct(jobItemList);

        getView().onSuccess("");
    }

    @Override
    public void getDistance(String origins, String destination) {
        serviceManager.getDistance("imperial", origins, destination, "AIzaSyDubyVjVoTC31vIbKIk7ggi2-vFZC3nFkc", new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("distance", result.toString());
                try {
                    JSONObject jsonObj = new JSONObject(result.toString());
                    Log.e("json array", jsonObj.getString("destination_addresses"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("distance", t.getLocalizedMessage());
            }
        });
    }
}
