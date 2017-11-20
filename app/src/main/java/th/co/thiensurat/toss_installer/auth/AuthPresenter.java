package th.co.thiensurat.toss_installer.auth;

import android.util.Log;


import com.hwangjr.rxbus.RxBus;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;
import th.co.thiensurat.toss_installer.auth.item.AuthenItemGroup;
import th.co.thiensurat.toss_installer.auth.item.ConvertAuthItem;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;


/**
 * Created by teerayut.k on 10/16/2017.
 */

public class AuthPresenter extends BaseMvpPresenter<AuthInterface.View> implements AuthInterface.Presenter {

    private ServiceManager serviceManager;

    public static AuthInterface.Presenter create() {
        return new AuthPresenter();
    }

    public AuthPresenter() {
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
    public void auth(List<RequestAuth.authenBody> itemAuths) {
        getView().onLoad();
        serviceManager.getAuth(itemAuths, new ServiceManager.ServiceManagerCallback<AuthItemResultGroup>() {
            @Override
            public void onSuccess(AuthItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    getView().onDismiss();
                    Log.e("Name", result.getData().get(0).getFirstname());
                    AuthenItemGroup authenItemGroup = ConvertAuthItem.createAuthItemGroupFromResult(result);
                    MyApplication.getInstance().getPrefManager().setProfile(authenItemGroup);
                    getView().onSuccess();
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
            }
        });
    }
}
