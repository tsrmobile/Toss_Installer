package th.co.thiensurat.toss_installer.mapcheckin;

import android.content.Context;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class MapCheckinInterface {

    public interface View extends BaseMvpInterface.View {
        void resultCheckin();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<MapCheckinInterface.View> {
        void saveImageUrl(Context context, String orderid, String type, String url);
        void checkImage(Context context, String orderid, String type);
    }
}
