package th.co.thiensurat.toss_installer.jobinstallation;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobInstallPresenter extends BaseMvpPresenter<JobInstallInterface.View> implements JobInstallInterface.Presenter {

    public static JobInstallInterface.Presenter create() {
        return new JobInstallPresenter();
    }
}
