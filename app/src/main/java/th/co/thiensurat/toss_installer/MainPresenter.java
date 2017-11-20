package th.co.thiensurat.toss_installer;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class MainPresenter extends BaseMvpPresenter<MainInterface.View> implements MainInterface.Presenter {

    public static MainInterface.Presenter create() {
        return new MainPresenter();
    }
}
