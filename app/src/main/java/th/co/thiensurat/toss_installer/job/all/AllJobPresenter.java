package th.co.thiensurat.toss_installer.job.all;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 12/5/2017.
 */

public class AllJobPresenter extends BaseMvpPresenter<AllJobInterface.View> implements AllJobInterface.Presenter {

    private DBHelper dbHelper;
    private JobItemGroup jobItemGroup;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public static AllJobInterface.Presenter create() {
        return new AllJobPresenter();
    }

    @Override
    public void getJobFromSqlite(Context context) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.jobItemGroup = dbHelper.getAllJob();

        jobItemList = jobItemGroup.getData();
        getView().setJobToCalendar(jobItemList);
    }

    @Override
    public void getJobByDate(Context context, String date) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.jobItemGroup = dbHelper.getAllJobByDate(date);

        jobItemList = jobItemGroup.getData();
        getView().setJobToCalendar(jobItemList);
    }
}
