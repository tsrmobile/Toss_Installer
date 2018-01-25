package th.co.thiensurat.toss_installer.contract.installReceipt;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 1/8/2018.
 */

public class InstallReceiptPresenter extends BaseMvpPresenter<InstallReceiptInterface.View> implements InstallReceiptInterface.Presenter {

    private DBHelper dbHelper;
    private ServiceManager serviceManager;
    private JobFinishItem jobFinishItem;
    private List<AddressItem> addressItems;
    private ProductItemGroup productItemGroup;

    public static InstallReceiptInterface.Presenter create() {
        return new InstallReceiptPresenter();
    }

    public InstallReceiptPresenter() {
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
    public void jobFinish(Context context, String orderid, String contno) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.setJobFinish(orderid, contno)) {
            getView().jobFinish(true);
        } else {
            getView().jobFinish(false);
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
}
