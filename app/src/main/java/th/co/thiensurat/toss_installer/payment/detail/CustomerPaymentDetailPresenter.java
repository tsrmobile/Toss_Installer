package th.co.thiensurat.toss_installer.payment.detail;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 3/15/2018.
 */

public class CustomerPaymentDetailPresenter extends BaseMvpPresenter<CustomerPaymentDetailInterface.View> implements CustomerPaymentDetailInterface.Presenter {

    private static Context context;
    private ServiceManager serviceManager;

    public static CustomerPaymentDetailInterface.Presenter create() {
        return new CustomerPaymentDetailPresenter();
    }

    public CustomerPaymentDetailPresenter() {
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
    public void updateDueDate(String orderid, String duedate) {
        serviceManager.requestUpdateDueDate(orderid, duedate, new ServiceManager.ServiceManagerCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
