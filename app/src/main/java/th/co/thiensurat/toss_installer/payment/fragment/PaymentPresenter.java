package th.co.thiensurat.toss_installer.payment.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertJobList;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

/**
 * Created by teerayut.k on 3/9/2018.
 */

public class PaymentPresenter extends BaseMvpPresenter<PaymentInterface.View> implements PaymentInterface.Presenter {

    private static Context context;
    private ServiceManager serviceManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public static PaymentInterface.Presenter create(FragmentActivity fragmentActivity) {
        context = fragmentActivity;
        return new PaymentPresenter();
    }

    public PaymentPresenter() {
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
    public void requestJobPayment(String data, String empid) {
        serviceManager.requestJobPayment(data, empid, new ServiceManager.ServiceManagerCallback<JobItemResultGroup>() {
            @Override
            public void onSuccess(JobItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    jobItemList = ConvertJobList.creatJobItemList(result.getData());
                    //setJobToTable(jobItemList);
                    getView().setJobPaymentToAdapter(jobItemList);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onFail(result.getMessage().toString());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onFail(result.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure current job", t.getLocalizedMessage());
            }
        });
    }
}
