package th.co.thiensurat.toss_installer.api.result.data;

import java.util.List;

/**
 * Created by teerayut.k on 12/16/2017.
 */

public class DataResultGroup {

    private String status;
    private String message;
    private List<DataResult> data;

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

    public List<DataResult> getData() {
        return data;
    }

    public void setData(List<DataResult> data) {
        this.data = data;
    }
}
