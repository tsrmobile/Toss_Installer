package th.co.thiensurat.toss_installer.api.result;


import java.util.List;

import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;


/**
 * Created by teerayut.k on 9/26/2017.
 */

public class JobItemResult {

    private String orderid;
    private String IDCard;
    private String title;
    private String firstName;
    private String lastName;
    private String installStartDate;
    private String installEndDate;
    private String installStart;
    private String installEnd;
    private String status;
    private String presale;
    private String contno;
    private String closeDate;
    private String duedate;
    private List<ProductItem> product;
    private List<AddressItem> address;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInstallStartDate() {
        return installStartDate;
    }

    public void setInstallStartDate(String installStartDate) {
        this.installStartDate = installStartDate;
    }

    public String getInstallEndDate() {
        return installEndDate;
    }

    public void setInstallEndDate(String installEndDate) {
        this.installEndDate = installEndDate;
    }

    public String getInstallStart() {
        return installStart;
    }

    public void setInstallStart(String installStart) {
        this.installStart = installStart;
    }

    public String getInstallEnd() {
        return installEnd;
    }

    public void setInstallEnd(String installEnd) {
        this.installEnd = installEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPresale() {
        return presale;
    }

    public void setPresale(String presale) {
        this.presale = presale;
    }

    public String getContno() {
        return contno;
    }

    public void setContno(String contno) {
        this.contno = contno;
    }

    public List<ProductItem> getProduct() {
        return product;
    }

    public void setProduct(List<ProductItem> product) {
        this.product = product;
    }

    public List<AddressItem> getAddress() {
        return address;
    }

    public void setAddress(List<AddressItem> address) {
        this.address = address;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }
}
