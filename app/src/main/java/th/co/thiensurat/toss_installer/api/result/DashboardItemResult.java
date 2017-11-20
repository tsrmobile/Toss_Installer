package th.co.thiensurat.toss_installer.api.result;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardItemResult {

    private int joblimit;
    private int jobnow;
    private int joball;
    private int jobsuccess;
    private int jobcount;

    public int getJoblimit() {
        return joblimit;
    }

    public void setJoblimit(int joblimit) {
        this.joblimit = joblimit;
    }

    public int getJobnow() {
        return jobnow;
    }

    public void setJobnow(int jobnow) {
        this.jobnow = jobnow;
    }

    public int getJoball() {
        return joball;
    }

    public void setJoball(int joball) {
        this.joball = joball;
    }

    public int getJobsuccess() {
        return jobsuccess;
    }

    public void setJobsuccess(int jobsuccess) {
        this.jobsuccess = jobsuccess;
    }

    public int getJobcount() {
        return jobcount;
    }

    public void setJobcount(int jobcount) {
        this.jobcount = jobcount;
    }
}
