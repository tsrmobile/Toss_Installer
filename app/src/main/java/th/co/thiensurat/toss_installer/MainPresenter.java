package th.co.thiensurat.toss_installer;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class MainPresenter extends BaseMvpPresenter<MainInterface.View> implements MainInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private ServiceManager serviceManager;

    public static MainInterface.Presenter create(Context activity) {
        context = activity;
        return new MainPresenter();
    }

    public MainPresenter() {
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
    public void getAddressNotSync() {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        getView().setAddressSync(dbHelper.getAddressNotSync(context));
    }

    @Override
    public void requestAddressSync(List<RequestUpdateAddress.updateBody> updateBodyList) {
        //getView().showNotificationSyncIcon();
        serviceManager.requestUpdateAddr(updateBodyList, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {

                    } else if ("FAIL".equals(jsonObject.getString("status"))) {

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
