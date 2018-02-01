package th.co.thiensurat.toss_installer.auth;

import android.content.Context;
import android.util.Log;


import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.api.result.data.ConvertData;
import th.co.thiensurat.toss_installer.api.result.data.DataResult;
import th.co.thiensurat.toss_installer.api.result.data.DataResultGroup;
import th.co.thiensurat.toss_installer.auth.item.AuthenItem;
import th.co.thiensurat.toss_installer.auth.item.AuthenItemGroup;
import th.co.thiensurat.toss_installer.auth.item.ConvertAuthItem;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.ConvertJobList;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;


/**
 * Created by teerayut.k on 10/16/2017.
 */

public class AuthPresenter extends BaseMvpPresenter<AuthInterface.View> implements AuthInterface.Presenter {

    private DBHelper dbHelper;
    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();
    private List<AuthenItem> authenItems = new ArrayList<AuthenItem>();
    private List<DataResultGroup> dataResultGroups = new ArrayList<DataResultGroup>();

    public static AuthInterface.Presenter create() {
        return new AuthPresenter();
    }

    public AuthPresenter() {
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
    public void auth(List<RequestAuth.authenBody> itemAuths) {
        getView().onLoad();
        serviceManager.getAuth(itemAuths, new ServiceManager.ServiceManagerCallback<AuthItemResultGroup>() {
            @Override
            public void onSuccess(AuthItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    AuthenItemGroup authenItemGroup = ConvertAuthItem.createAuthItemGroupFromResult(result);
                    authenItems = authenItemGroup.getData();
                    MyApplication.getInstance().getPrefManager().setProfile(authenItemGroup);
                    getView().onDismiss();
                    getView().onSuccess();
                    /*if (authenItems.get(0).getPositionName().equals("ช่างติดตั้งผลิตภัณฑ์")) {
                        getView().onInstall();
                    } else if (authenItems.get(0).getPositionName().equals("พนักงานเก็บเงิน")) {
                        getView().onPayment();
                    } else {
                        //getView().onDismiss();
                        //getView().onFail("ตำแหน่งของคุณ\nไม่สามารถใช้แอพฯนี้ได้");
                        getView().onInstall();
                    }*/
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
            }
        });
    }

    @Override
    public void Jobrequest(String data, String empid) {
        serviceManager.getJob(data, empid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    jobItemList = ConvertJobList.creatJobItemList(result.getData());
                    //getView().onDismiss();
                    getView().insertToSqlite(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDismiss();
                    getView().onNextPage();
                    //getView().onFail(result.getMessage().toString());
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
    public void insetToSqlite(Context context, List<JobItem> jobItemList) {
        try {
            if (MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRST_OPEN).equals("opened")) {
                getView().onDismiss();
                getView().onNextPage();
            } else {
                dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
                if (dbHelper.isTableExists(Constance.TABLE_JOB) || dbHelper.isTableExists(Constance.TABLE_ADDRESS)
                        || dbHelper.isTableExists(Constance.TABLE_PRODUCT) || dbHelper.isTableExists(Constance.TABLE_IMAGE)
                        || dbHelper.isTableExists(Constance.TABLE_INSTALL_ITEM)) {
                    dbHelper.emptyTable(Constance.TABLE_JOB);
                    dbHelper.emptyTable(Constance.TABLE_ADDRESS);
                    dbHelper.emptyTable(Constance.TABLE_PRODUCT);
                    dbHelper.emptyTable(Constance.TABLE_IMAGE);
                    dbHelper.emptyTable(Constance.TABLE_INSTALL_ITEM);
                }

                dbHelper.setTableJob(jobItemList);
                //dbHelper.setTableAddress(jobItemList);
                dbHelper.setTableProduct(jobItemList);
                MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_FIRST_OPEN, "opened");
                getView().onDismiss();
                getView().onNextPage();
            }
        } catch (Exception ex) {
            dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
            if (dbHelper.isTableExists(Constance.TABLE_JOB) || dbHelper.isTableExists(Constance.TABLE_ADDRESS)
                    || dbHelper.isTableExists(Constance.TABLE_PRODUCT) || dbHelper.isTableExists(Constance.TABLE_IMAGE)
                    || dbHelper.isTableExists(Constance.TABLE_INSTALL_ITEM)) {
                dbHelper.emptyTable(Constance.TABLE_JOB);
                dbHelper.emptyTable(Constance.TABLE_ADDRESS);
                dbHelper.emptyTable(Constance.TABLE_PRODUCT);
                dbHelper.emptyTable(Constance.TABLE_IMAGE);
                dbHelper.emptyTable(Constance.TABLE_INSTALL_ITEM);
            }

            dbHelper.setTableJob(jobItemList);
            //dbHelper.setTableAddress(jobItemList);
            dbHelper.setTableProduct(jobItemList);
            MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_FIRST_OPEN, "opened");
            getView().onDismiss();
            getView().onNextPage();
        }
    }

    @Override
    public void Paymentrequest(String data, String empid) {

    }

    @Override
    public void insertPaymentToSqlite(Context context) {

    }
}
