package th.co.thiensurat.toss_installer.contract;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class ContractPresenter extends BaseMvpPresenter<ContractInterface.View> implements ContractInterface.Presenter {

    private static Context context;
    private DBHelper dbHelper;
    private ServiceManager serviceManager;
    private List<AddressItem> addressItems;
    private ProductItemGroup productItemGroup;

    public static ContractInterface.Presenter create(Context activity) {
        context = activity;
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
    public void getAddressFromSQLite(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddessFromSQLite(addressItems);
    }

    @Override
    public void getProductFromSQLite(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.productItemGroup = dbHelper.getProductByID(orderid);
        getView().setProductFromSQLite(productItemGroup.getProduct());
    }

    @Override
    public void getContactNumber(String orderid) {
        serviceManager.requestContact(orderid, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().setContactNumber(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
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
    public void updateContactNumber(String orderid, String contactnumber) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateContactNumberToProduct(orderid, contactnumber);
    }

    @Override
    public void updateStep(String orderid, String step) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateStep(orderid, step);
        getView().onJobClosed();
    }

    @Override
    public void getAllImage(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        getView().setImageToContactBody(dbHelper.getAllImageURI(orderid));
    }

    @Override
    public void uploadImageToServer(RequestBody requestBody, List<MultipartBody.Part> body) {
        getView().onLongLoad();
        serviceManager.requestUpload(requestBody, body, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().uploadData();
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    getView().onDismiss();
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                Log.e("fail", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public String getInstallDate(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.getInstallDate(orderid);
    }

    @Override
    public String getInstallEnd(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.getInstallEnd(orderid);
    }

    @Override
    public void updatePrintStatus(String orderid, String print) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updatePrintStatus(orderid, print);
    }

    @Override
    public void uploadDataToServer(RequestBody requestBody) {
        serviceManager.requestUploadData(requestBody, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onUploadSuccess(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    getView().onDismiss();
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                Log.e("fail", t.getLocalizedMessage());
            }
        });
    }
}
