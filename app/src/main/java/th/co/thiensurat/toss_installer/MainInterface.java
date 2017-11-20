package th.co.thiensurat.toss_installer;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class MainInterface {

    public interface View extends BaseMvpInterface.View {

    }

    public interface Presenter extends BaseMvpInterface.Presenter<MainInterface.View> {

    }
}
