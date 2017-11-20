package th.co.thiensurat.toss_installer.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 9/26/2017.
 */

public class JobItemResultGroup {

    private String status;
    private String message;
    private List<JobItemResult> data;

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

    public List<JobItemResult> getData() {
        return data;
    }

    public void setData(List<JobItemResult> data) {
        this.data = data;
    }
}
