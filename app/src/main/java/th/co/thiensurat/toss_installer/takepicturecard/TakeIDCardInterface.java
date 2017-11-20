package th.co.thiensurat.toss_installer.takepicturecard;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;

/**
 * Created by teerayut.k on 11/17/2017.
 */

public class TakeIDCardInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoading();
        void onDismiss();
        void setImageToAdapter(List<ImageItem> imageItems);
        void refresh();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<TakeIDCardInterface.View> {
        void saveImageUrl(Context context, String orderid, String type, String url);
        void getImage(Context context, String orderid, String type);
        void editImageUrl(Context context, String id, String url);
        void delImage(Context context, String id);
    }
}
