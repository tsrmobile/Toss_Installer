package th.co.thiensurat.toss_installer.contract;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.api.result.ContactResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.contract.item.JobSuccessItem;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class ContractInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddessFromSQLite(List<AddressItem> itemList);
        void setProductFromSQLite(List<ProductItem> productItemList);
        void setContactNumber(String number);
        void updatejobFinishSuccess(boolean boo);
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<ContractInterface.View> {
        void getAddressFromSQLite(Context context, String orderid);
        void getProductFromSQLite(Context context, String orderid);

        void getContactNumber();
        void updatejobFinish(Context context, String orderid, String contno);
        JobFinishItem getFinishData(Context context, String orderid, String contno);
        void requestUpdateJobFinish(JobFinishItem jobFinishItem);

        String getContno(Context context, String orderid);
        List<JobSuccessItem> getDataSuccess(Context context, String orderid);

    }
}
