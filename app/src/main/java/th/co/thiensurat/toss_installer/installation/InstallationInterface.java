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
        void setProductDetail(List<ProductItem> productItemList);
        void refreshProduct();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<InstallationInterface.View> {
        void getProductDetail(Context context, String orderid);
        void setProductItemGroup(ProductItemGroup itemGroup);
        ProductItemGroup getProductItemGroup();
        void setProductItemToAdapter(ProductItemGroup productItemGroup);

        void updateProduct(Context context, String id, String serial);
    }
}
