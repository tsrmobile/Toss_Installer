package th.co.thiensurat.toss_installer.mapcheckin;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class MapCheckinPresenter extends BaseMvpPresenter<MapCheckinInterface.View> implements MapCheckinInterface.Presenter {

    public static MapCheckinInterface.Presenter create() {
        return new MapCheckinPresenter();
    }
}
