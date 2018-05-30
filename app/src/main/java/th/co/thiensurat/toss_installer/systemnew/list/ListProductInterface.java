package th.co.thiensurat.toss_installer.systemnew.list;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

public class ListProductInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setListToAdapter(List<JobItem> jobItems);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<ListProductInterface.View> {
        void getList(String data, String empid);
    }
}
