package th.co.thiensurat.toss_installer.systemnew.detail;

import java.util.List;

import okhttp3.RequestBody;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;

public class NewDetailInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void setDetailToView(List<JobItem> jobItems);
        void onSuccess(String success);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<NewDetailInterface.View> {
        void getDetail(String orderid);
        void updateData(RequestBody requestBody);
    }
}
