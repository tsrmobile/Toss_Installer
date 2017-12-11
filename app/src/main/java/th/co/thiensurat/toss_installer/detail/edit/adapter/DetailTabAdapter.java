package th.co.thiensurat.toss_installer.detail.edit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.co.thiensurat.toss_installer.detail.edit.addresscard.CardAddressFragment;
import th.co.thiensurat.toss_installer.detail.edit.addressinstall.InstallAddressFragment;
import th.co.thiensurat.toss_installer.detail.edit.addresspayment.PaymentAddressFragment;

/**
 * Created by teerayut.k on 9/19/2017.
 */

public class DetailTabAdapter extends FragmentStatePagerAdapter {

    private int tab_count = 0;

    public DetailTabAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tab_count = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return CardAddressFragment.getInstance();
            case 1 :
                return InstallAddressFragment.getInstance();
            case 2 :
                return PaymentAddressFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tab_count;
    }
}
