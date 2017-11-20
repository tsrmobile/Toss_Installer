package th.co.thiensurat.toss_installer.dashboard.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardItem extends BaseItem implements Parcelable {

    private int joblimit;
    private int jobnow;
    private int joball;
    private int jobsuccess;
    private int jobcount;

    public DashboardItem() {

    }

    public int getJoblimit() {
        return joblimit;
    }

    public DashboardItem setJoblimit(int joblimit) {
        this.joblimit = joblimit;
        return this;
    }

    public int getJobnow() {
        return jobnow;
    }

    public DashboardItem setJobnow(int jobnow) {
        this.jobnow = jobnow;
        return this;
    }

    public int getJoball() {
        return joball;
    }

    public DashboardItem setJoball(int joball) {
        this.joball = joball;
        return this;
    }

    public int getJobsuccess() {
        return jobsuccess;
    }

    public DashboardItem setJobsuccess(int jobsuccess) {
        this.jobsuccess = jobsuccess;
        return this;
    }

    public int getJobcount() {
        return jobcount;
    }

    public DashboardItem setJobcount(int jobcount) {
        this.jobcount = jobcount;
        return this;
    }

    protected DashboardItem(Parcel in) {
        super(in);
        joblimit        = in.readInt();
        jobnow          = in.readInt();
        joball          = in.readInt();
        jobsuccess      = in.readInt();
        jobcount        = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(joblimit);
        dest.writeInt(jobnow);
        dest.writeInt(joball);
        dest.writeInt(jobsuccess);
        dest.writeInt(jobcount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        DashboardItem item = new DashboardItem()
                .setJoblimit(joblimit)
                .setJobnow(jobnow)
                .setJoball(joball)
                .setJobsuccess(jobsuccess)
                .setJobcount(jobcount);
        return item;
    }

    public static final Creator<DashboardItem> CREATOR = new Creator<DashboardItem>() {
        @Override
        public DashboardItem createFromParcel(Parcel in) {
            return new DashboardItem(in);
        }

        @Override
        public DashboardItem[] newArray(int size) {
            return new DashboardItem[size];
        }
    };
}
