package th.co.thiensurat.toss_installer.contract.installReceipt;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;

/**
 * Created by teerayut.k on 1/8/2018.
 */

public class InstallReceiptInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddessFromSQLite(List<AddressItem> itemList);
        void jobFinish(boolean boo);
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<InstallReceiptInterface.View> {
        void getAddressFromSQLite(Context context, String orderid);
        void jobFinish(Context context, String orderid, String contno);
        JobFinishItem getFinishData(Context context, String orderid, String contno);
        void requestUpdateJobFinish(JobFinishItem jobFinishItem);
    }
}
