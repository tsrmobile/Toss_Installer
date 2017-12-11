package th.co.thiensurat.toss_installer.job;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class JobInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setJobItemToAdapter(List<JobItem> itemList);
        void setNewDataToSQLite(List<JobItem> itemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<JobInterface.View> {
        void Jobrequest(String data, String empid, String location);
        void setJobItemGroup(JobItemGroup itemGroup);
        JobItemGroup getJobItemGroup();
        void setJobItemToAdapter(JobItemGroup jobItemGroup);

        //void insertDataToSQLite(Context context, List<JobItem> jobItemList);
        void getJobFromSqlite(Context context, String date);

        void insertNewData(Context context, List<JobItem> jobItemList);
    }
}
