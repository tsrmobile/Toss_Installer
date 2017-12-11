package th.co.thiensurat.toss_installer.job.all;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.JobItem;

/**
 * Created by teerayut.k on 12/5/2017.
 */

public class AllJobInterface {

    public interface View extends BaseMvpInterface.View {
        void setJobToCalendar(List<JobItem> itemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<AllJobInterface.View> {
        void getJobFromSqlite(Context context);
    }
}
