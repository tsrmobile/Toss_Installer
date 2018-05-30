package th.co.thiensurat.toss_installer.payment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.co.thiensurat.toss_installer.payment.paymentfragment.paymentjob.PaymentFragment;
import th.co.thiensurat.toss_installer.payment.paymentfragment.paymentsuccess.PaymentSuccessFragment;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class PaymentViewAdapter extends FragmentStatePagerAdapter {

    public PaymentViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return PaymentFragment.getInstance();
            /*case 1 :
                return PaymentSuccessFragment.getInstance();*/
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
