package th.co.thiensurat.toss_installer.contract;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.ContactResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.contract.item.JobSuccessItem;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class ContractPresenter extends BaseMvpPresenter<ContractInterface.View> implements ContractInterface.Presenter {

    private DBHelper dbHelper;
    private ServiceManager serviceManager;
    private JobFinishItem jobFinishItem;
    private List<AddressItem> addressItems;
    private ProductItemGroup productItemGroup;

    public static ContractInterface.Presenter create() {
        return new ContractPresenter();
    }

    public ContractPresenter() {
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
    public void getAddressFromSQLite(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddessFromSQLite(addressItems);
    }

    @Override
    public void getProductFromSQLite(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.productItemGroup = dbHelper.getProductByID(orderid);
        getView().setProductFromSQLite(productItemGroup.getProduct());
    }

    @Override
    public void getContactNumber() {
        serviceManager.requestContact(new ServiceManager.ServiceManagerCallback<ContactResultGroup>() {
            @Override
            public void onSuccess(ContactResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    getView().setContactNumber(result.getData());
                } else if (result.getStatus().equals("FAIL")) {

                } else if (result.getStatus().equals("ERROR")) {

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void updatejobFinish(Context context, String orderid, String contno) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.setJobFinish(orderid, contno)) {
            getView().updatejobFinishSuccess(true);
        } else {
            getView().updatejobFinishSuccess(false);
        }
    }

    @Override
    public JobFinishItem getFinishData(Context context, String orderid, String contno) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        jobFinishItem = dbHelper.getFinishData(orderid, contno);
        return jobFinishItem;
    }

    @Override
    public void requestUpdateJobFinish(JobFinishItem jobFinishItem) {
        getView().onLoad();
        serviceManager.requestUpdateJobFinish("finish", jobFinishItem.getOrderid(), jobFinishItem.getInstallstart(),
                jobFinishItem.getInstallend(), jobFinishItem.getUsercode(), new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onSuccess(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    } else {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("fail", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public String getContno(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.checkContno(orderid);
    }

    @Override
    public List<JobSuccessItem> getDataSuccess(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.getDataSuccess(orderid);
    }
}
