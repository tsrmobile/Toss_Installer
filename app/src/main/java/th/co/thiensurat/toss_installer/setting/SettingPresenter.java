package th.co.thiensurat.toss_installer.setting;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 1/22/2018.
 */

public class SettingPresenter extends BaseMvpPresenter<SettingInterface.View> implements SettingInterface.Presenter {

    public static SettingInterface.Presenter create() {
        return new SettingPresenter();
    }
}
