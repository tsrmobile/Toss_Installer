package th.co.thiensurat.toss_installer.api.request;

import java.util.List;

/**
 * Created by teerayut.k on 1/11/2018.
 */

public class RequestFinish {

    private String orderid;
    private String contno;
    private String installstart;
    private String installend;
    private String status;
    private String empid;
    private List<RequestFinishProduct> products;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getContno() {
        return contno;
    }

    public void setContno(String contno) {
        this.contno = contno;
    }

    public String getInstallstart() {
        return installstart;
    }

    public void setInstallstart(String installstart) {
        this.installstart = installstart;
    }

    public String getInstallend() {
        return installend;
    }

    public void setInstallend(String installend) {
        this.installend = installend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public List<RequestFinishProduct> getProducts() {
        return products;
    }

    public void setProducts(List<RequestFinishProduct> products) {
        this.products = products;
    }
}
