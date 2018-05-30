package th.co.thiensurat.toss_installer;

import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

public class Main2Presenter extends BaseMvpPresenter<Main2Interface.View> implements Main2Interface.Presenter {

    private ServiceManager serviceManager;

    public static Main2Interface.Presenter create() {
        return new Main2Presenter();
    }

    public Main2Presenter() {
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
    public void updateCurrentLocation(String id, double lat, double lon) {
        serviceManager.updateLatLon(id, lat, lon, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        Log.e("msg", jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        Log.e("msg", jsonObject.getString("message"));
                    } else {
                        Log.e("msg", jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("json obj", t.getLocalizedMessage());
            }
        });
    }
}
