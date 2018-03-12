package th.co.thiensurat.toss_installer.takepicturhome;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;

/**
 * Created by teerayut.k on 11/17/2017.
 */

public class TakeHomeInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoading();
        void onDismiss();
        void setImageToAdapter(List<ImageItem> imageItems);
        void refresh();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void saveImageUrl(String orderid, String type, String url, String productcode);
        void getImage(String orderid, String type);
        void editImageUrl(String id, String url);
        void delImage(String id);
        void updateStep(String orderid, String step);
    }
}
