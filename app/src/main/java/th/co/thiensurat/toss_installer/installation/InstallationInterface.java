package th.co.thiensurat.toss_installer.installation;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.job.item.ProductItemGroup;

/**
 * Created by teerayut.k on 11/12/2017.
 */

public class InstallationInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setProductDetail(List<ProductItem> productItemList);
        void refreshProduct();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<InstallationInterface.View> {
        boolean checkSerial(Context context, String serial, String productcode);
        void updateSerialToServer(String orderid, String productcode, String serial);

        boolean checkItem(Context context);
        void getProductDetail(Context context, String orderid);
        void setProductItemGroup(ProductItemGroup itemGroup);
        void setProductItemToAdapter(ProductItemGroup productItemGroup);
        ProductItemGroup getProductItemGroup();

        void updateProduct(Context context, String id, String serial);
        //boolean checkPackageInstall(Context context, String orderid, String productcode);*/
    }
}
