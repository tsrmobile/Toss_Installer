package th.co.thiensurat.toss_installer.api.request;

import java.util.List;

public class RequestPayment {

    private List<paymentBody> body;

    public List<paymentBody> getBody() {
        return body;
    }

    public RequestPayment setBody(List<paymentBody> body) {
        this.body = body;
        return this;
    }

    public static class paymentBody {
        private String contno;
        private String productcode;
        private String period;
        private String paymenttype;
        private String paymentreceive;
        private String duedate;
        private String paydate;
        private String empid;
        private String amount;
        private String actual;
        private String receiptno;
        private String receiptdate;

        public String getContno() {
            return contno;
        }

        public paymentBody setContno(String contno) {
            this.contno = contno;
            return this;
        }

        public String getProductcode() {
            return productcode;
        }

        public paymentBody setProductcode(String productcode) {
            this.productcode = productcode;
            return this;
        }

        public String getPeriod() {
            return period;
        }

        public paymentBody setPeriod(String period) {
            this.period = period;
            return this;
        }

        public String getPaymenttype() {
            return paymenttype;
        }

        public paymentBody setPaymenttype(String paymenttype) {
            this.paymenttype = paymenttype;
            return this;
        }

        public String getPaymentreceive() {
            return paymentreceive;
        }

        public paymentBody setPaymentreceive(String paymentreceive) {
            this.paymentreceive = paymentreceive;
            return this;
        }

        public String getDuedate() {
            return duedate;
        }

        public paymentBody setDuedate(String duedate) {
            this.duedate = duedate;
            return this;
        }

        public String getPaydate() {
            return paydate;
        }

        public paymentBody setPaydate(String paydate) {
            this.paydate = paydate;
            return this;
        }

        public String getEmpid() {
            return empid;
        }

        public paymentBody setEmpid(String empid) {
            this.empid = empid;
            return this;
        }

        public String getAmount() {
            return amount;
        }

        public paymentBody setAmount(String amount) {
            this.amount = amount;
            return this;
        }

        public String getActual() {
            return actual;
        }

        public paymentBody setActual(String actual) {
            this.actual = actual;
            return this;
        }

        public String getReceiptno() {
            return receiptno;
        }

        public paymentBody setReceiptno(String receiptno) {
            this.receiptno = receiptno;
            return this;
        }

        public String getReceiptdate() {
            return receiptdate;
        }

        public paymentBody setReceiptdate(String receiptdate) {
            this.receiptdate = receiptdate;
            return this;
        }
    }
}
