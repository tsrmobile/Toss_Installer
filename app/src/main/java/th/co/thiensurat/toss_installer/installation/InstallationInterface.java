package th.co.thiensurat.toss_installer.installation;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;

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

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        boolean checkSerial(String serial, String productcode);
        void updateSerialToServer(String orderid, String productcode, String serial);

        boolean checkItem();
        void getProductDetail(String orderid);
        void setProductItemGroup(ProductItemGroup itemGroup);
        void updateProduct(String id, String serial);
        void updateStep(String orderid, String step);
        void setProductItemToAdapter(ProductItemGroup productItemGroup);
        ProductItemGroup getProductItemGroup();
    }
}
