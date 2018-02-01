package th.co.thiensurat.toss_installer.takepicture;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
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

    public interface Presenter extends BaseMvpInterface.Presenter<TakePictureInterface.View> {
        void saveImageUrl(Context context, String orderid, String serial, String type, String url, String productcode);
        void getImage(Context context, String orderid, String serial, String type);
        void editImageUrl(Context context, String id, String url);
        void delImage(Context context, String id);
        boolean getItemInstalled(Context context, String orderid);
        List<ProductItem> getAllItem(Context context, String orderid);

        void uploadImageToServer(String action, String orderid, String image64, String imageType, String productcode);
    }
}
