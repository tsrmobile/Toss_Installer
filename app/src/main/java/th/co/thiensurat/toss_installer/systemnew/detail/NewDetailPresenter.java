package th.co.thiensurat.toss_installer.systemnew.detail;

import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertItemToGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

public class NewDetailPresenter extends BaseMvpPresenter<NewDetailInterface.View> implements NewDetailInterface.Presenter {

    private ServiceManager serviceManager;
    private List<JobItem> jobItemList;

    public NewDetailPresenter() {
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

    public static NewDetailInterface.Presenter create() {
        return new NewDetailPresenter();
    }

    @Override
    public void getDetail(String orderid) {
        //getView().onLoad();
        serviceManager.getDetailList(orderid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    //getView().onDismiss();
                    jobItemList = ConvertItemToGroup.creatJobItemList(result.getData());
                    getView().setDetailToView(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    //getView().onDismiss();
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    //getView().onDismiss();
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //getView().onDismiss();
                Log.e("Failure current job", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void updateData(RequestBody requestBody) {
        getView().onLoad();
        serviceManager.closeJob(requestBody, new ServiceManager.ServiceManagerCallback() {
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
                    }
                } catch (JSONException e) {
                    getView().onDismiss();
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                Log.e("Failure current job", t.getLocalizedMessage());
            }
        });
    }
}
