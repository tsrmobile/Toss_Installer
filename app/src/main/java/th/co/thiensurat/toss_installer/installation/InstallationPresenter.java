package th.co.thiensurat.toss_installer.installation;

import android.content.Context;
import android.util.Log;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.job.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/12/2017.
 */

public class InstallationPresenter extends BaseMvpPresenter<InstallationInterface.View> implements InstallationInterface.Presenter {

    private DBHelper dbHelper;
    private ProductItemGroup productItemGroup;
    private List<ProductItem> productItemList;

    public static InstallationInterface.Presenter create() {
        return new InstallationPresenter();
    }

    @Override
    public void getProductDetail(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.productItemGroup = dbHelper.getProductByID(orderid);
        setProductItemGroup(productItemGroup);
        this.productItemList = productItemGroup.getProduct();
        getView().setProductDetail(productItemList);
    }

    @Override
    public void setProductItemGroup(ProductItemGroup itemGroup) {
        this.productItemGroup = itemGroup;
    }

    @Override
    public ProductItemGroup getProductItemGroup() {
        return productItemGroup;
    }

    @Override
    public void setProductItemToAdapter(ProductItemGroup productItemGroup) {
        getView().setProductDetail(productItemGroup.getProduct());
    }

    @Override
    public void updateProduct(Context context, String id, String serial) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateSerialToTableProduct(id, serial);
        getView().refreshProduct();
    }

    @Override
    public boolean checkItem(Context context) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.checkItemExisting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkSerial(Context context, String serial, String productcode) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.checkItemSerial(serial, productcode)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkPackageInstall(Context context, String orderid, String productcode) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        if (dbHelper.getProductPackage(orderid, productcode)) {
            return true;
        } else {
            return false;
        }
    }
}
