package th.co.thiensurat.toss_installer.api.result.data;

/**
 * Created by teerayut.k on 12/16/2017.
 */

public class DataItem {

    private String dataId;
    private String dataCode;
    private String dataName;
    private String dataFK;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataFK() {
        return dataFK;
    }

    public void setDataFK(String dataFK) {
        this.dataFK = dataFK;
    }
}
