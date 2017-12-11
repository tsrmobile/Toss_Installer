package th.co.thiensurat.toss_installer.mapcheckin.result;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class CheckinResultInterface {

    public interface View extends BaseMvpInterface.View {
        void setImageToAdapter(List<ImageItem> imageItems);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<CheckinResultInterface.View> {
        void getImage(Context context, String orderid, String type);
    }
}
