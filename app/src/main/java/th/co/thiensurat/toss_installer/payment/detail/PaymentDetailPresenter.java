package th.co.thiensurat.toss_installer.payment.detail;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class PaymentDetailPresenter extends BaseMvpPresenter<PaymentDetailInterface.View> implements PaymentDetailInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private ServiceManager serviceManager;
    private List<AddressItem> addressItems;

    public static PaymentDetailInterface.Presenter create(Context activity) {
        context = activity;
        return new PaymentDetailPresenter();
    }

    public PaymentDetailPresenter() {
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
    public void updateDueDate(String orderid, String duedate, String empid) {
        getView().onLoad();
        serviceManager.requestUpdateDueDate(orderid, duedate, empid, new ServiceManager.ServiceManagerCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onDueSuccess(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    } else if ("ERROR".equals(jsonObject.getString("status"))) {
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

    /*@Override
    public void setAddressDetail(String orderid, List<AddressItem> addressItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.setTableAddressDetial(orderid, addressItemList);
    }*/

    @Override
    public void getReceiptNumber(String contno) {
        serviceManager.requestReceiptNumber("getreceipt", contno, new ServiceManager.ServiceManagerCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().setReceiptNumber(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onFail(jsonObject.getString("message"));
                    } else if ("ERROR".equals(jsonObject.getString("status"))) {
                        getView().onFail(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("fail gerReceipt", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void addToLocalDB(List<RequestPayment.paymentBody> paymentBodies) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.setTablePayment(paymentBodies);
    }

    @Override
    public void addContractPayment(List<RequestPayment.paymentBody> paymentBodies) {
        getView().onLoad();
        serviceManager.requestPayment(paymentBodies, new ServiceManager.ServiceManagerCallback<Object>() {
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
