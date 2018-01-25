package th.co.thiensurat.toss_installer.detail.edit.addresspayment;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;
import th.co.thiensurat.toss_installer.utils.db.ExDBHelper;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class PaymentAddressPresenter extends BaseMvpPresenter<PaymentAddressInterface.View> implements PaymentAddressInterface.Presenter {

    private DBHelper dbHelper;
    private ExDBHelper exDBHelper;
    private List<AddressItem> addressItems;
    private AddressItemGroup itemGroup;
    private ServiceManager serviceManager;
    private AddressItemGroup addressItemGroup = new AddressItemGroup();

    public static PaymentAddressInterface.Presenter create() {
        return new PaymentAddressPresenter();
    }

    public PaymentAddressPresenter() {
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
        addressItemGroup.setData(addressItems);
        setAddressItemGroup(addressItemGroup);
        getView().setAddressDetail(addressItems);
    }

    @Override
    public void getInfo(Context context, String infoType, String id) {
        exDBHelper = new ExDBHelper(context);
        try {
            exDBHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (infoType) {
            case "province" :
                getView().setInfoToAdapter(infoType, exDBHelper.getAllProvince());
                break;
            case "district" :
                getView().setInfoToAdapter(infoType, exDBHelper.getDistrictByProvince(id));
                break;
            case "subdistrict" :
                getView().setInfoToAdapter(infoType, exDBHelper.getSubdistrictByDistrict(id));
                break;
            case "zipcode" :
                getView().setZipcode(exDBHelper.getZipcode(id));
                break;
        }
    }

    @Override
    public void setAddressItemGroup(AddressItemGroup addressItemGroup) {
        this.itemGroup = addressItemGroup;
    }

    @Override
    public AddressItemGroup getAddressItemGroup() {
        return itemGroup;
    }

    @Override
    public void setAddressItemToAdapter(AddressItemGroup addressItem) {
        getView().setAddressDetail(addressItem.getData());
    }

    @Override
    public void updateData(Context context, String orderid, String type, List<AddressItem> addressItemList) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.updateAddress(orderid, type, addressItemList)) {
            getView().updateLocalSuccess(true);
        } else {
            getView().updateLocalSuccess(false);
        }
    }

    @Override
    public void updateDataOnline(List<RequestUpdateAddress.updateBody> updateBody) {
        getView().OnLoad();
        serviceManager.requestUpdateAddr(updateBody, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().OnDismiss();
                        getView().OnSuccess(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().OnDismiss();
                        getView().OnFail(jsonObject.getString("message"));
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
