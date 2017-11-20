package th.co.thiensurat.toss_installer.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItemResultGroup {

    private String status;
    private String message;
    private List<InstallItemResult> data;

    public InstallItemResultGroup() {

    }

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

    public List<InstallItemResult> getData() {
        return data;
    }

    public void setData(List<InstallItemResult> data) {
        this.data = data;
    }
}
