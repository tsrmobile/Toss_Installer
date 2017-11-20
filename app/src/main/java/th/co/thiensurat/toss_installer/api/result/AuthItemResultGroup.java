package th.co.thiensurat.toss_installer.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 10/18/2017.
 */

public class AuthItemResultGroup {

    private String status;
    private String message;
    private List<AuthItemResult> data;

    public AuthItemResultGroup() {

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

    public List<AuthItemResult> getData() {
        return data;
    }

    public void setData(List<AuthItemResult> data) {
        this.data = data;
    }
}
