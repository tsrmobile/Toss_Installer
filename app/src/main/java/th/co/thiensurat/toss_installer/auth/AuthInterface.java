package th.co.thiensurat.toss_installer.auth;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.JobItem;


/**
 * Created by teerayut.k on 10/16/2017.
 */

public class AuthInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess();
        void insertToSqlite(List<JobItem> jobItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void auth(List<RequestAuth.authenBody> itemAuths);

        void Jobrequest(String data, String empid);
        void insetToSqlite(Context context, List<JobItem> jobItemList);

    }
}
