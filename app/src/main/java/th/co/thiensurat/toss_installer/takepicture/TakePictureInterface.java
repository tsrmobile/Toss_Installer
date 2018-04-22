package th.co.thiensurat.toss_installer.takepicture;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;

/**
 * Created by teerayut.k on 11/12/2017.
 */

public class TakePictureInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoading();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void setImageToAdapter(List<ImageItem> imageItems);
        void refresh();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void saveImageUrl(String orderid, String serial, String type, String url, String productcode);
        void getImage(String orderid, String type);
        void editImageUrl(String id, String url);
        void delImage(String id);
        boolean getItemInstalled(String orderid);
        List<ProductItem> getAllItem(String orderid);
        void updateStep(String orderid, String step);

        String getProductPayType(String orderid, String productCode, String serial);
        //void uploadImageToServer(String action, String orderid, String image64, String imageType, String productcode);
    }
}
