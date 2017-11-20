package th.co.thiensurat.toss_installer.utils;

import android.view.animation.AlphaAnimation;

/**
 * Created by teera-s on 11/16/2016 AD.
 */

public class AnimateButton {

    AlphaAnimation animation;

    public AlphaAnimation animbutton() {
        animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(50);
        animation.setStartOffset(100);
        animation.setFillAfter(true);

        return animation;
    }
}
