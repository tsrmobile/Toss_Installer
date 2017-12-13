package th.co.thiensurat.toss_installer.detail;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class DetailPresenter extends BaseMvpPresenter<DetailInterface.View> implements DetailInterface.Presenter {

    private DBHelper dbHelper;
    private ServiceManager serviceManager;
    private List<AddressItem> addressItems;

    public static DetailInterface.Presenter create() {
        return new DetailPresenter();
    }

    public DetailPresenter() {
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
    public void getAddressDetail(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddressDetail(addressItems);
    }

    @Override
    public void setCancelJob(Context context, String orderid, String cancelnote) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.setCancelJob(orderid, cancelnote)) {
            //getView().setCancelSuccess();
            requestUpdate(cancelnote, "91", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID), orderid);
        } else {
            getView().onFail("ยกเลิกไม่สำเร็จ กรุณาติดต่อผู้พัฒนา");
        }
    }

    public void requestUpdate(String note, String status, String empid, String orderid) {
        getView().onLoad();
        serviceManager.requestCancel(note, status, empid, orderid, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().setCancelSuccess();
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
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
