package th.co.thiensurat.toss_installer.installation;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/12/2017.
 */

public class InstallationPresenter extends BaseMvpPresenter<InstallationInterface.View> implements InstallationInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private ProductItemGroup productItemGroup;
    private List<ProductItem> productItemList;

    private ServiceManager serviceManager;

    public InstallationPresenter() {
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

    public static InstallationInterface.Presenter create(Context activity) {
        context = activity;
        return new InstallationPresenter();
    }

    @Override
    public void getProductDetail(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.productItemGroup = dbHelper.getProductByID(orderid);
        setProductItemGroup(productItemGroup);
        this.productItemList = productItemGroup.getProduct();
        getView().setProductDetail(productItemList);
    }

    @Override
    public void setProductItemGroup(ProductItemGroup itemGroup) {
        this.productItemGroup = itemGroup;
    }

    @Override
    public void updateSerialToServer(String orderid, String productcode, String serial) {
        getView().onLoad();
        serviceManager.requestUpdateSerial("serial", orderid, productcode, serial, new ServiceManager.ServiceManagerCallback() {
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
    public boolean checkSerial(String serial, String productcode) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.checkItemSerial(serial, productcode);
    }

    @Override
    public ProductItemGroup getProductItemGroup() {
        return productItemGroup;
    }

    @Override
    public void setProductItemToAdapter(ProductItemGroup productItemGroup) {
        getView().setProductDetail(productItemGroup.getProduct());
    }

    @Override
    public boolean checkItem() {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.checkItemExisting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void updateProduct(String id, String serial) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateSerialToTableProduct(id, serial);
        getView().refreshProduct();
    }

    @Override
    public void updateStep(String orderid, String step) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateStep(orderid, step);
    }

    /*@Override
    public void updateStatus(String action, String orderid) {
        serviceManager.requestUPdateStatus(action, orderid, new ServiceManager.ServiceManagerCallback() {
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
    }*/
}
