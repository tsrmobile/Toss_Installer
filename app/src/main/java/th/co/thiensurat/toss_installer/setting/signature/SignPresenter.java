package th.co.thiensurat.toss_installer.setting.signature;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 1/22/2018.
 */

public class SignPresenter extends BaseMvpPresenter<SignInterface.View> implements SignInterface.Presenter {

    public static SignInterface.Presenter create() {
        return new SignPresenter();
    }
}
