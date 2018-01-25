package th.co.thiensurat.toss_installer.api.result;

/**
 * Created by teerayut.k on 12/28/2017.
 */

public class ContactResultGroup {

    public String status;
    public String message;
    public String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
