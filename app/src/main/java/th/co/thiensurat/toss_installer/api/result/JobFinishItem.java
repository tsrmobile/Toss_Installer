package th.co.thiensurat.toss_installer.api.result;

/**
 * Created by teerayut.k on 1/3/2018.
 */

public class JobFinishItem {

    private String orderid;
    private String installstart;
    private String installend;
    private String status;
    private String usercode;
    private String contno;

    public String getOrderid() {
        return orderid;
    }

    public JobFinishItem setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getInstallstart() {
        return installstart;
    }

    public JobFinishItem setInstallstart(String installstart) {
        this.installstart = installstart;
        return this;
    }

    public String getInstallend() {
        return installend;
    }

    public JobFinishItem setInstallend(String installend) {
        this.installend = installend;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public JobFinishItem setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getUsercode() {
        return usercode;
    }

    public JobFinishItem setUsercode(String usercode) {
        this.usercode = usercode;
        return this;
    }

    public String getContno() {
        return contno;
    }

    public JobFinishItem setContno(String contno) {
        this.contno = contno;
        return this;
    }
}
