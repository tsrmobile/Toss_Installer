package th.co.thiensurat.toss_installer.jobinstallation;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobInstallInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoading();
        void onFail(String fail);
        void onDimiss();
        void onSuccess();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void getAllJob();
        void setJobToDB(List<JobItem> jobItemList);
    }
}
