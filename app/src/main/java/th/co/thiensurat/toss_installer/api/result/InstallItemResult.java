package th.co.thiensurat.toss_installer.api.result;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItemResult {

    private String PrintTakeStockID;
    private String Product_SerialNum;
    private String Ref_Date;
    private String Product_Code;
    private String Product_Name;
    private String AStockStatus;

    public InstallItemResult() {

    }

    public String getPrintTakeStockID() {
        return PrintTakeStockID;
    }

    public void setPrintTakeStockID(String printTakeStockID) {
        PrintTakeStockID = printTakeStockID;
    }

    public String getProduct_SerialNum() {
        return Product_SerialNum;
    }

    public void setProduct_SerialNum(String product_SerialNum) {
        Product_SerialNum = product_SerialNum;
    }

    public String getRef_Date() {
        return Ref_Date;
    }

    public void setRef_Date(String ref_Date) {
        Ref_Date = ref_Date;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public void setProduct_Code(String product_Code) {
        Product_Code = product_Code;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getAStockStatus() {
        return AStockStatus;
    }

    public void setAStockStatus(String AStockStatus) {
        this.AStockStatus = AStockStatus;
    }
}
