package th.co.thiensurat.toss_installer.contract.item;

import java.util.List;

/**
 * Created by teerayut.k on 2/14/2018.
 */

public class ContactItem {

    private String orderid;
    private String empid;
    private String installdate;
    private String installend;
    private List<ObjectImage> images;

    public String getOrderid() {
        return orderid;
    }

    public ContactItem setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getEmpid() {
        return empid;
    }

    public ContactItem setEmpid(String empid) {
        this.empid = empid;
        return this;
    }

    public String getInstalldate() {
        return installdate;
    }

    public ContactItem setInstalldate(String installdate) {
        this.installdate = installdate;
        return this;
    }

    public String getInstallend() {
        return installend;
    }

    public ContactItem setInstallend(String installend) {
        this.installend = installend;
        return this;
    }

    public List<ObjectImage> getImages() {
        return images;
    }

    public ContactItem setImages(List<ObjectImage> images) {
        this.images = images;
        return this;
    }
}
