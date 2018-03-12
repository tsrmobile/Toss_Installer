package th.co.thiensurat.toss_installer.jobinstallation.current;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class CurrentJobInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setJobItemToAdapter(List<JobItem> jobItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void getCurrentJob(String data, String empid);
        void getCurrentJobLocalDB();
        void setJobToTable(List<JobItem> jobItemList);
    }
}
