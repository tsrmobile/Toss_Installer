package th.co.thiensurat.toss_installer.api.result;


import java.util.List;

import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;

/**
 * Created by teerayut.k on 9/26/2017.
 */

public class JobItemResult {

    private String orderid;
    private String IDCard;
    private String title;
    private String firstName;
    private String lastName;
    private String contactphone;
    /*private String productCode;
    private String productName;
    private String productQty;*/
    private String installStartDate;
    private String installEndDate;
    //private AddressItem address;
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

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    /*public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }*/

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

    /*public AddressItem getAddress() {
        return address;
    }

    public void setAddress(AddressItem address) {
        this.address = address;
    }*/

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
}
