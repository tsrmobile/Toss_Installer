package th.co.thiensurat.toss_installer.jobinstallation.finish;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.JobItem;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobFinishInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setJobItemToAdapter(List<JobItem> jobItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<JobFinishInterface.View> {
        void getJobFinish(String data, String empid);
    }
}
