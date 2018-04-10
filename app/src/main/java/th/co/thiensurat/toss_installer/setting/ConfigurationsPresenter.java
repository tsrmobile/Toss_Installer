package th.co.thiensurat.toss_installer.setting;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 3/13/2018.
 */

public class ConfigurationsPresenter extends BaseMvpPresenter<ConfigurationInterface.View> implements ConfigurationInterface.Presenter {

    public static ConfigurationInterface.Presenter create() {
        return new ConfigurationsPresenter();
    }
}
