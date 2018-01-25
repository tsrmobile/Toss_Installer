package th.co.thiensurat.toss_installer.contract.item;

import java.util.List;

/**
 * Created by teerayut.k on 1/23/2018.
 */

public class JobSuccessItem {

    private String orderID;
    private String contno;
    private String installStartDate;
    private String installEndDate;
    private String installerID;
    private String taxinvoice;
    private List<ProductSuccessItem> product;

    public String getOrderID() {
        return orderID;
    }

    public JobSuccessItem setOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public String getContno() {
        return contno;
    }

    public JobSuccessItem setContno(String contno) {
        this.contno = contno;
        return this;
    }

    public String getInstallStartDate() {
        return installStartDate;
    }

    public JobSuccessItem setInstallStartDate(String installStartDate) {
        this.installStartDate = installStartDate;
        return this;
    }

    public String getInstallEndDate() {
        return installEndDate;
    }

    public JobSuccessItem setInstallEndDate(String installEndDate) {
        this.installEndDate = installEndDate;
        return this;
    }

    public String getInstallerID() {
        return installerID;
    }

    public JobSuccessItem setInstallerID(String installerID) {
        this.installerID = installerID;
        return this;
    }

    public String getTaxinvoice() {
        return taxinvoice;
    }

    public JobSuccessItem setTaxinvoice(String taxinvoice) {
        this.taxinvoice = taxinvoice;
        return this;
    }

    public List<ProductSuccessItem> getProduct() {
        return product;
    }

    public JobSuccessItem setProduct(List<ProductSuccessItem> product) {
        this.product = product;
        return this;
    }
}
