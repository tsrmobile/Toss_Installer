package th.co.thiensurat.toss_installer.takepicturecheckin;

import android.content.Context;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class MapCheckinInterface {

    public interface View extends BaseMvpInterface.View {
        void resultCheckin();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void saveImageUrl(String orderid, String type, String url, String productcode);
        void checkImage(String orderid, String type);
        void editImageUrl(String id, String url);
    }
}
