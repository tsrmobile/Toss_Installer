package th.co.thiensurat.toss_installer.jobinstallation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.co.thiensurat.toss_installer.jobinstallation.current.CurrentJobFragment;
import th.co.thiensurat.toss_installer.jobinstallation.finish.JobFinishFragment;
import th.co.thiensurat.toss_installer.jobinstallation.unfinish.JobUnFinishFragment;

/**
 * Created by teerayut.k on 1/25/2018.
 */

public class JobViewAdapter extends FragmentStatePagerAdapter {

    public JobViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                return CurrentJobFragment.getInstance();
            case 1 :
                return JobUnFinishFragment.getInstance();
            case 2 :
                return JobFinishFragment.getInstance();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
