package th.co.thiensurat.toss_installer.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardItemResultGroup {

    private String status;
    private String message;
    private List<DashboardItemResult> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DashboardItemResult> getData() {
        return data;
    }

    public void setData(List<DashboardItemResult> data) {
        this.data = data;
    }
}
